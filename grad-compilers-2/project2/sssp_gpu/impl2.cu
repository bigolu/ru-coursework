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

__global__ void cudaInitIntArray(int *a, int len, int val){
    int totalThreads = gridDim.x * blockDim.x;
    int totalWarps = (totalThreads % 32 == 0) ?  totalThreads / 32 : totalThreads / 32 + 1;
    int threadId = blockDim.x * blockIdx.x + threadIdx.x;
    int warpId = threadId / 32;
    int laneId = threadId % 32;
    int load = (len % totalWarps == 0) ? len / totalWarps : len / totalWarps + 1;
    int beg = load * warpId;
    int end = (len < beg + load) ? len : beg + load;
    beg = beg + laneId;

    for(int i = beg; i < end; i += 32){
        a[i] = val;
    }
}

/****** END UTIL METHODS ******/

int INF = INT_MAX;
int ZERO = 0;

__global__ void setToMin(int *distPrev, int *distCur, int distLen){ 
    int totalThreads = gridDim.x * blockDim.x;
    int totalWarps = (totalThreads % 32 == 0) ?  totalThreads / 32 : totalThreads / 32 + 1;
    int threadId = blockDim.x * blockIdx.x + threadIdx.x;
    int warpId = threadId / 32;
    int laneId = threadId % 32;
    int load = (distLen % totalWarps == 0) ? distLen / totalWarps : distLen / totalWarps + 1;
    int beg = load * warpId;
    int end = (distLen < beg + load) ? distLen : beg + load;
    beg = beg + laneId;

    for(int i = beg; i < end; i += 32){
        atomicMin(&distCur[i], distPrev[i]);
        atomicMin(&distPrev[i], distCur[i]);
    }
}

__global__ void filterEdges(edge *edges, int edgesLen, edge *toProcessEdges, int *x, int *updated){
    int totalThreads = gridDim.x * blockDim.x;
    int totalWarps = (totalThreads % 32 == 0) ?  totalThreads / 32 : totalThreads / 32 + 1;
    int threadId = blockDim.x * blockIdx.x + threadIdx.x;
    int warpId = threadId / 32;
    int laneId = threadId % 32;
    int load = (edgesLen % totalWarps == 0) ? edgesLen / totalWarps : edgesLen / totalWarps + 1;
    int beg = load * warpId;
    int end = (edgesLen < beg + load) ? edgesLen : beg + load;
    beg = beg + laneId;
    int curOffset = x[warpId];

    for(int i = beg; i < end; i += 32){
        int src = edges[i].src;
        int mask = __ballot(updated[src]);
        int localId = __popc(mask<<(32-laneId));
        if(updated[src]){
            memcpy(&toProcessEdges[localId + curOffset], &edges[i], sizeof(edge));
        }
        curOffset += __popc(mask);
    }
}

__global__ void getExcPrefixSum(int *x, int *toProcessLen, int totalWarps){
    int prevSum = 0;
    for(int i = 0; i < totalWarps; i++){
        int tmp = x[i];
        x[i] = prevSum;
        prevSum += tmp;
    }

    // update length of toProcess edges
    *toProcessLen = prevSum;
}

__global__ void getNumToProcess(edge *edges, int edgesLen, int *x, int *updated){
    int totalThreads = gridDim.x * blockDim.x;
    int totalWarps = (totalThreads % 32 == 0) ?  totalThreads / 32 : totalThreads / 32 + 1;
    int threadId = blockDim.x * blockIdx.x + threadIdx.x;
    int warpId = threadId / 32;
    int laneId = threadId % 32;
    int load = (edgesLen % totalWarps == 0) ? edgesLen / totalWarps : edgesLen / totalWarps + 1;
    int beg = load * warpId;
    int end = (edgesLen < beg + load) ? edgesLen : beg + load;
    beg = beg + laneId;

    for(int i = beg; i < end; i += 32){
        int src = edges[i].src;
        int mask = __ballot(updated[src]);

        if(laneId == 0){
            x[warpId] += __popc(mask);
        }
    }
}

__global__ void processEdges2(edge *edges, int *edgesLen, int *distPrev, int *distCur, int *hasUpdated, int *updated){
    int totalThreads = gridDim.x * blockDim.x;
    int totalWarps = (totalThreads % 32 == 0) ?  totalThreads / 32 : totalThreads / 32 + 1;
    int threadId = blockDim.x * blockIdx.x + threadIdx.x;
    int warpId = threadId / 32;
    int laneId = threadId % 32;
    int load = (*edgesLen % totalWarps == 0) ? *edgesLen / totalWarps : *edgesLen / totalWarps + 1;
    int beg = load * warpId;
    int end = (*edgesLen < beg + load) ? *edgesLen : beg + load;
    beg = beg + laneId;

    for(int i = beg; i < end; i += 32){
        int src = edges[i].src;
        int dest = edges[i].dest;
        int weight = edges[i].weight;

        // avoid overflow when adding weight to src
        if(distPrev[src] == INT_MAX)
            continue;
        
        int dist = distPrev[src] + weight;
        if(dist < distPrev[dest]){
            atomicMin(&distCur[dest], dist);
            *hasUpdated = 1;
            updated[dest] = 1;
        }
    }
}

__global__ void processEdgesIncore2(edge *edges, int *edgesLen, int *dist, int *hasUpdated, int *updated){
    int totalThreads = gridDim.x * blockDim.x;
    int totalWarps = (totalThreads % 32 == 0) ?  totalThreads / 32 : totalThreads / 32 + 1;
    int threadId = blockDim.x * blockIdx.x + threadIdx.x;
    int warpId = threadId / 32;
    int laneId = threadId % 32;
    int load = (*edgesLen % totalWarps == 0) ? *edgesLen / totalWarps : *edgesLen / totalWarps + 1;
    int beg = load * warpId;
    int end = (*edgesLen < beg + load) ? *edgesLen : beg + load;
    beg = beg + laneId;

    for(int i = beg; i < end; i += 32){
        int src = edges[i].src;
        int dest = edges[i].dest;
        int weight = edges[i].weight;

        // avoid overflow when adding weight to src
        if(dist[src] == INT_MAX)
            continue;
        
        int tmpDist = dist[src] + weight;
        if(tmpDist < dist[dest]){
            atomicMin(&dist[dest], tmpDist);
            *hasUpdated = 1;
            updated[dest] = 1;
        }
    }
}

void bmfOutcore2(std::vector<edge> edges, int blockSize, int blockNum){
    int numVertices = getNumVertices(edges);
    int numEdges = edges.size();
    int totalThreads = blockSize * blockNum;
    int warpNum = (totalThreads % 32 == 0) ? totalThreads / 32 : totalThreads / 32 + 1;
    int distanceVectorSize = sizeof(int) * numVertices;

    edge *edges_d;
    cudaMalloc((void**)&edges_d, numEdges * sizeof(edge));
    cudaMemcpy(edges_d, edges.data(), edges.size()*sizeof(edge), cudaMemcpyHostToDevice);

    int *distCur;
    cudaMalloc((void**)&distCur, distanceVectorSize);
    cudaMemcpy((void*)distCur, &ZERO, sizeof(int), cudaMemcpyHostToDevice);
    cudaInitIntArray<<<blockNum, blockSize>>>(&distCur[1], numVertices - 1, INF);

    int *distPrev;
    cudaMalloc((void**)&distPrev, distanceVectorSize);
    cudaMemcpy((void*)distPrev, &ZERO, sizeof(int), cudaMemcpyHostToDevice);
    cudaInitIntArray<<<blockNum, blockSize>>>(&distPrev[1], numVertices - 1, INF);

    int *hasUpdated;
    cudaMalloc((void**)&hasUpdated, sizeof(int));
    cudaMemcpy((void*)hasUpdated, &ZERO, sizeof(int), cudaMemcpyHostToDevice);

    int *x; 
    cudaMalloc((void**)&x, sizeof(int) * warpNum);
    cudaInitIntArray<<<blockNum, blockSize>>>(x, warpNum, ZERO);

    edge *toProcessEdges;
    cudaMalloc((void**)&toProcessEdges, numEdges * sizeof(edge));
    cudaMemcpy(toProcessEdges, edges.data(), edges.size()*sizeof(edge), cudaMemcpyHostToDevice);

    int *toProcessEdgesLen;
    cudaMalloc((void**)&toProcessEdgesLen, sizeof(int));
    cudaMemcpy(toProcessEdgesLen, &numEdges, sizeof(int), cudaMemcpyHostToDevice);

    int *updated;
    cudaMalloc((void**)&updated, sizeof(int) * numVertices);
    cudaInitIntArray<<<blockNum, blockSize>>>(updated, numVertices, ZERO);

    // start BMF
    while(true){
        processEdges2<<<blockNum, blockSize>>>(toProcessEdges, toProcessEdgesLen, distPrev, distCur, hasUpdated, updated);
        getNumToProcess<<<blockNum, blockSize>>>(edges_d, numEdges, x, updated);
        getExcPrefixSum<<<1, 1>>>(x, toProcessEdgesLen, warpNum);
        filterEdges<<<blockNum, blockSize>>>(edges_d, numEdges, toProcessEdges, x, updated);
        setToMin<<<blockNum, blockSize>>>(distPrev, distCur, numVertices);

        if(!readCudaInt(hasUpdated))
            break;

        // reset for next iteration
        cudaInitIntArray<<<blockNum, blockSize>>>(x, warpNum, ZERO);
        cudaInitIntArray<<<blockNum, blockSize>>>(updated, numVertices, ZERO);
        cudaMemcpy((void*)hasUpdated, &ZERO, sizeof(int), cudaMemcpyHostToDevice);
        swap((void**)&distCur, (void**)&distPrev);
    }

    // write answer to file
    int *output = (int*) malloc(distanceVectorSize);
    cudaMemcpy((void*)output, distCur, distanceVectorSize, cudaMemcpyDeviceToHost);
    writeAnswer(output, numVertices);

    // free up
    cudaFree(edges_d);
    cudaFree(distPrev);
    cudaFree(distCur);
    cudaFree(hasUpdated);
    cudaFree(x);
    cudaFree(toProcessEdges);
    cudaFree(toProcessEdgesLen);
    cudaFree(updated);
    free(output);
}

void bmfIncore2(std::vector<edge> edges, int blockSize, int blockNum){
    int numVertices = getNumVertices(edges);
    int numEdges = edges.size();
    int totalThreads = blockSize * blockNum;
    int warpNum = (totalThreads % 32 == 0) ? totalThreads / 32 : totalThreads / 32 + 1;
    int distanceVectorSize = sizeof(int) * numVertices;

    edge *edges_d;
    cudaMalloc((void**)&edges_d, numEdges * sizeof(edge));
    cudaMemcpy(edges_d, edges.data(), edges.size()*sizeof(edge), cudaMemcpyHostToDevice);

    int *dist;
    cudaMalloc((void**)&dist, distanceVectorSize);
    cudaMemcpy((void*)dist, &ZERO, sizeof(int), cudaMemcpyHostToDevice);
    cudaInitIntArray<<<blockNum, blockSize>>>(&dist[1], numVertices - 1, INF);

    int *hasUpdated;
    cudaMalloc((void**)&hasUpdated, sizeof(int));
    cudaMemcpy((void*)hasUpdated, &ZERO, sizeof(int), cudaMemcpyHostToDevice);

    int *x; 
    cudaMalloc((void**)&x, sizeof(int) * warpNum);
    cudaInitIntArray<<<blockNum, blockSize>>>(x, warpNum, ZERO);

    edge *toProcessEdges;
    cudaMalloc((void**)&toProcessEdges, numEdges * sizeof(edge));
    cudaMemcpy(toProcessEdges, edges.data(), edges.size()*sizeof(edge), cudaMemcpyHostToDevice);

    int *toProcessEdgesLen;
    cudaMalloc((void**)&toProcessEdgesLen, sizeof(int));
    cudaMemcpy(toProcessEdgesLen, &numEdges, sizeof(int), cudaMemcpyHostToDevice);

    int *updated;
    cudaMalloc((void**)&updated, sizeof(int) * numVertices);
    cudaInitIntArray<<<blockNum, blockSize>>>(updated, numVertices, ZERO);

    // start BMF
    while(true){
        processEdgesIncore2<<<blockNum, blockSize>>>(toProcessEdges, toProcessEdgesLen, dist, hasUpdated, updated);
        getNumToProcess<<<blockNum, blockSize>>>(edges_d, numEdges, x, updated);
        getExcPrefixSum<<<1, 1>>>(x, toProcessEdgesLen, warpNum);
        filterEdges<<<blockNum, blockSize>>>(edges_d, numEdges, toProcessEdges, x, updated);

        if(!readCudaInt(hasUpdated))
            break;

        // reset for next iteration
        cudaInitIntArray<<<blockNum, blockSize>>>(x, warpNum, ZERO);
        cudaInitIntArray<<<blockNum, blockSize>>>(updated, numVertices, ZERO);
        cudaMemcpy((void*)hasUpdated, &ZERO, sizeof(int), cudaMemcpyHostToDevice);
    }

    // write answer to file
    int *output = (int*) malloc(distanceVectorSize);
    cudaMemcpy((void*)output, dist, distanceVectorSize, cudaMemcpyDeviceToHost);
    writeAnswer(output, numVertices);

    // free up
    cudaFree(edges_d);
    cudaFree(dist);
    cudaFree(hasUpdated);
    cudaFree(x);
    cudaFree(toProcessEdges);
    cudaFree(toProcessEdgesLen);
    cudaFree(updated);
    free(output);
}

void neighborHandler(std::vector<edge> * edgesPtr, int blockSize, int blockNum, int outcore){
    setTime();

    if(!((blockSize * blockNum <= 2048) && (blockNum <= 64))){
        puts("ERROR: blockNum must be <= 64 and total threads must be <= 2048\n");
        return;
    }
    std::vector<edge> edges = *edgesPtr;
    std::sort(edges.begin(), edges.end(), edgeSrcComparator);
    outcore ? bmfOutcore2(edges, blockSize, blockNum) : bmfIncore2(edges, blockSize, blockNum);

    cudaDeviceProp props; cudaGetDeviceProperties(&props, 0);
    printf("The total computation kernel time on GPU %s is %f milli-seconds\n", props.name, getTime());
}
