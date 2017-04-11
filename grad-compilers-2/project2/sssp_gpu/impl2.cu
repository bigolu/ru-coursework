#include <vector>
#include <iostream>
#include <algorithm>

#include "utils.h"
#include "cuda_error_check.cuh"
#include "graph.h"
#include "parse_graph.hpp"
#include "limits.h"

/****** START UTIL METHODS ******/
bool edgeSrcComparator(edge a, edge b){ return (a.src < b.src); }
bool edgeDestComparator(edge a, edge b){ return (a.dest < b.dest); }

void swap(void **a, void **b){
    void *tmp = *a;
    *a = *b;
    *b = tmp;
}

int readCudaInt(int *i){
    int tmp;
    cudaMemcpy(&tmp, i, sizeof(int), cudaMemcpyDeviceToHost);
    
    return tmp;
}

void printEdges(std::vector<edge> edges){
    for(edge e : edges){
        printf("src: %d, dst: %d, weight: %d\n", e.src, e.dest, e.weight);
    }
}

int getNumVertices(std::vector<edge> edges){
    int max = -1;
    for(edge e : edges){
        int tmp = std::max(e.src, e.dest);
        max = std::max(max, tmp);
    }

    return max + 1;
}

void writeAnswer(int *output, int len){
    FILE *fp = fopen("output.txt", "w");
    for(int i = 0; i < len; i++){
        fprintf(fp, "%d:\t%d\n", i, output[i]);
    }
    fclose(fp);
}

__global__ void cudaInitIntArray(int *a, int *len, int *val){
    int totalThreads = gridDim.x * blockDim.x;
    int threadId = blockDim.x * blockIdx.x + threadIdx.x;
    int warpNum = (totalThreads % 32 == 0) ? 
        totalThreads / 32 : totalThreads / 32 + 1;
    int warpId = threadId / 32;
    int laneId = threadId % 32;
    int load = (*len % warpNum == 0) ? *len / warpNum : *len / warpNum + 1;
    int beg = load * warpId;
    int end = (int)fminf((float)*len, (float)beg + (float)load);
    beg = beg + laneId;

    for(int i = beg; i < end; i += 32){
        a[i] = *val;
    }
}

/****** END UTIL METHODS ******/

int INF = INT_MAX;
int ZERO = 0;

__global__ void fixInf(int *distPrev, int *distCur, int *len){ 
    int totalThreads = gridDim.x * blockDim.x;
    int threadId = blockDim.x * blockIdx.x + threadIdx.x;
    int warpNum = (totalThreads % 32 == 0) ? 
        totalThreads / 32 : totalThreads / 32 + 1;
    int warpId = threadId / 32;
    int laneId = threadId % 32;
    int load = (*len % warpNum == 0) ? *len / warpNum : *len / warpNum + 1;
    int beg = load * warpId;
    int end = (int)fminf((float)*len, (float)beg + (float)load);
    beg = beg + laneId;

    for(int i = beg; i < end; i += 32){
        atomicMin(&distCur[i], distPrev[i]);
        atomicMin(&distPrev[i], distCur[i]);
    }
}

__global__ void step3(edge *edges, int *len, edge *T, int *distPrev, int *distCur, int *x, int *updated){
    int totalThreads = gridDim.x * blockDim.x;
    int threadId = blockDim.x * blockIdx.x + threadIdx.x;
    int warpNum = (totalThreads % 32 == 0) ? 
        totalThreads / 32 : totalThreads / 32 + 1;
    int warpId = threadId / 32;
    int laneId = threadId % 32;
    int load = (*len % warpNum == 0) ? *len / warpNum : *len / warpNum + 1;
    int beg = load * warpId;
    int end = (int)fminf((float)*len, (float)beg + (float)load);
    int curOffset = x[warpId];
    beg = beg + laneId;

    for(int i = beg; i < end; i += 32){
        int src = edges[i].src;
        bool hasChanged = distPrev[src] != distCur[src];
        int mask = __ballot(updated[src]);
        int localId = __popc(mask<<(32-laneId));
        if(updated[src]){
            memcpy(&T[localId + curOffset], &edges[i], sizeof(edge));
            //printf("tsrc: %d, tdest: %d, tweight: %d\n", T[localId + curOffset].src, T[localId + curOffset].dest, T[localId + curOffset].weight);
        }

        __syncthreads();
        curOffset += __popc(mask);
    }
}

__global__ void step2(int *x, int *len, int *warpNum){
    int prevSum = 0;
    for(int i = 0; i < *warpNum; i++){
        int tmp = x[i];
        x[i] = prevSum;
        prevSum += tmp;

        if(i == (*warpNum - 1)){
            *len = prevSum;
            printf("NEWL: %d", *len);
        }
    }
}

__global__ void step1(edge *edges, int *len, int *distPrev, int *distCur, int *x, int *updated){
    int totalThreads = gridDim.x * blockDim.x;
    int threadId = blockDim.x * blockIdx.x + threadIdx.x;
    int warpNum = (totalThreads % 32 == 0) ? 
        totalThreads / 32 : totalThreads / 32 + 1;
    int warpId = threadId / 32;
    int laneId = threadId % 32;
    int load = (*len % warpNum == 0) ? *len / warpNum : *len / warpNum + 1;
    int beg = load * warpId;
    int end = (int)fminf((float)*len, (float)beg + (float)load);
    beg = beg + laneId;

    for(int i = beg; i < end; i += 32){
        int src = edges[i].src;
        bool hasChanged = distPrev[src] != distCur[src];
        int mask = __ballot(updated[src]);

        if(laneId == 0){
            x[warpId] += __popc(mask) * (laneId == 0);
        }
    }
}

__global__ void outcoreKernel2(edge *edges, int *len, int *distPrev, int *distCur, int *hasUpdated, int *updated){
    if(threadIdx.x == 0 && blockIdx.x == 0){
        *hasUpdated = 0;
    }
    __syncthreads();

    int load = (*len % gridDim.x == 0) ? *len / gridDim.x : *len / gridDim.x + 1;
    int beg = load * blockIdx.x;
    int end = (int)fminf((float)*len, (float)beg + (float)load);
    beg = beg + threadIdx.x;
    for(int i = beg; i < end; i += blockDim.x){
        int src = edges[i].src;
        int dest = edges[i].dest;
        int weight = edges[i].weight;
        if(distPrev[src] == INT_MAX) continue;
        if(distPrev[src] + weight < distPrev[dest]){
            atomicMin(&distCur[dest], distPrev[src] + weight);
            *hasUpdated = 1;
            updated[dest] = 1;
        }
        else if(distPrev[src] + weight == distPrev[dest]){
            atomicMin(&distCur[dest], distPrev[src] + weight);
        }
    }
}

void outcoreHost2(std::vector<edge> edges, int blockSize, int blockNum){
    int numVertices = getNumVertices(edges);
    int numVertices2 = numVertices - 1;
    int numEdges = edges.size();
    int distanceVectorSize = sizeof(int) * numVertices;
    int totalThreads = blockSize * blockNum;
    int warpNum = (totalThreads % 32 == 0) ?
        totalThreads / 32 : totalThreads / 32 + 1;

    // init kernel Args
    edge *edgesDev; int *len; int *distCur; int *distPrev; int *hasUpdated; int *x; int *warpNumDev; edge *T; int *newLen; int *numV; int *tmpLen; int *zeroDev; int *infDev; int *updated;
    cudaMalloc((void**)&edgesDev, numEdges * sizeof(edge));
    cudaMalloc((void**)&T, numEdges * sizeof(edge));
    cudaMalloc((void**)&len, sizeof(int));
    cudaMalloc((void**)&newLen, sizeof(int));
    cudaMalloc((void**)&tmpLen, sizeof(int));
    cudaMalloc((void**)&numV, sizeof(int));
    cudaMalloc((void**)&warpNumDev, sizeof(int));
    cudaMalloc((void**)&infDev, sizeof(int));
    cudaMalloc((void**)&zeroDev, sizeof(int));
    cudaMalloc((void**)&distCur, distanceVectorSize);
    cudaMalloc((void**)&distPrev, distanceVectorSize);
    cudaMalloc((void**)&hasUpdated, sizeof(int));
    cudaMalloc((void**)&x, sizeof(int) * warpNum);
    cudaMalloc((void**)&updated, sizeof(int) * numVertices);

    cudaMemcpy(edgesDev, edges.data(), edges.size()*sizeof(edge), cudaMemcpyHostToDevice);
    cudaMemcpy(T, edges.data(), edges.size()*sizeof(edge), cudaMemcpyHostToDevice);
    cudaMemcpy(len, &numEdges, sizeof(int), cudaMemcpyHostToDevice);
    cudaMemcpy(newLen, &numEdges, sizeof(int), cudaMemcpyHostToDevice);
    cudaMemcpy(numV, &numVertices, sizeof(int), cudaMemcpyHostToDevice);
    cudaMemcpy(warpNumDev, &warpNum, sizeof(int), cudaMemcpyHostToDevice);
    cudaMemcpy((void*)hasUpdated, &ZERO, sizeof(int), cudaMemcpyHostToDevice);
    cudaMemcpy((void*)infDev, &INF, sizeof(int), cudaMemcpyHostToDevice);
    cudaMemcpy((void*)zeroDev, &ZERO, sizeof(int), cudaMemcpyHostToDevice);

    cudaMemcpy((void*)tmpLen, &numVertices2, sizeof(int), cudaMemcpyHostToDevice);
    cudaMemcpy((void*)distCur, &ZERO, sizeof(int), cudaMemcpyHostToDevice);
    cudaInitIntArray<<<blockNum, blockSize>>>(&distCur[1], tmpLen, infDev);

    cudaMemcpy((void*)distPrev, &ZERO, sizeof(int), cudaMemcpyHostToDevice);
    cudaInitIntArray<<<blockNum, blockSize>>>(&distPrev[1], tmpLen, infDev);
    
    cudaMemcpy((void*)tmpLen, &warpNum, sizeof(int), cudaMemcpyHostToDevice);
    cudaInitIntArray<<<blockNum, blockSize>>>(x, tmpLen, zeroDev);

    cudaInitIntArray<<<blockNum, blockSize>>>(updated, numV, zeroDev);
    
    // launch kernels
    while(true){
        outcoreKernel2<<<blockNum, blockSize>>>(T, newLen, distPrev, distCur, hasUpdated, updated);
        step1<<<blockNum, blockSize>>>(edgesDev, len, distPrev, distCur, x, updated);
        step2<<<1, 1>>>(x, newLen, warpNumDev);
        step3<<<blockNum, blockSize>>>(edgesDev, len, T, distPrev, distCur, x, updated);
        if(!readCudaInt(hasUpdated))
            break;
        swap((void**)&distCur, (void**)&distPrev);
        cudaMemcpy((void*)tmpLen, &warpNum, sizeof(int), cudaMemcpyHostToDevice);
        cudaInitIntArray<<<blockNum, blockSize>>>(x, tmpLen, zeroDev);
        cudaInitIntArray<<<blockNum, blockSize>>>(updated, numV, zeroDev);
        fixInf<<<blockNum, blockSize>>>(distPrev, distCur, numV);
    }
    fixInf<<<blockNum, blockSize>>>(distPrev, distCur, numV);

    // copy output from device
    int *output = (int*) malloc(distanceVectorSize);
    cudaMemcpy((void*)output, distCur, distanceVectorSize, cudaMemcpyDeviceToHost);

    writeAnswer(output, numVertices);
}

void neighborHandler(std::vector<edge> * edgesPtr, int blockSize, int blockNum, int outcore){
    setTime();

    if(!((blockSize * blockNum <= 2048) && (blockNum < 64))){
        puts("ERROR: blockNum must be <= 64 and total threads must be <= 2048\n");
        return;
    }
    std::vector<edge> edges = *edgesPtr;
    std::sort(edges.begin(), edges.end(), edgeSrcComparator);
    outcore ? outcoreHost2(edges, blockSize, blockNum) : outcoreHost2(edges, blockSize, blockNum);

    cudaDeviceProp props; cudaGetDeviceProperties(&props, 0);
    printf("The total computation kernel time on GPU %s is %f milli-seconds\n", props.name, getTime());
}
