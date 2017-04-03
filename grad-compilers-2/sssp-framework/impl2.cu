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
/****** END UTIL METHODS ******/

int INF = INT_MAX;
int ZERO = 0;

__global__ void outcoreKernel(edge *edges, int *len, int *distPrev, int *distCur, int *hasUpdated){
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
        }
        else if(distPrev[src] + weight == distPrev[dest]){
            atomicMin(&distCur[dest], distPrev[src] + weight);
        }
    }
}

__global__ void filterEdges(edge *edges, int *len, edge *edges2, int *distPrev, int *distCur){
    int newLen = 0;
    for(int i = 0; i < *len; i++){
        int srcIndex = edges[i].src;
        if((distPrev[srcIndex] != distCur[srcIndex]) 
                || (distPrev[srcIndex] == INT_MAX)){
            //memcpy((void*)&edges2[newLen], (void*)&edges[i], sizeof(edge));
            newLen++;
        }
    }
    *len = newLen;
    printf("%d\n", newLen);
    printf("%d\n", *len);
}

void neighborHandlerHost(std::vector<edge> edges, int blockSize, int blockNum){
    int numVertices = getNumVertices(edges);
    int numEdges = edges.size();
    int distanceVectorSize = sizeof(int) * numVertices;

    // init kernel Args
    edge *edgesDev; int *len; int *distCur; int *distPrev; int *hasUpdated; edge *edgesDev2;
    cudaMalloc((void**)&edgesDev, numEdges * sizeof(edge));
    cudaMalloc((void**)&edgesDev2, numEdges * sizeof(edge));
    cudaMalloc((void**)&len, sizeof(int));
    cudaMalloc((void**)&distCur, distanceVectorSize);
    cudaMalloc((void**)&distPrev, distanceVectorSize);
    cudaMalloc((void**)&hasUpdated, sizeof(int));

    cudaMemcpy(edgesDev, edges.data(), edges.size()*sizeof(edge), cudaMemcpyHostToDevice);
    cudaMemcpy(len, &numEdges, sizeof(int), cudaMemcpyHostToDevice);
    cudaMemcpy((void*)hasUpdated, &ZERO, sizeof(int), cudaMemcpyHostToDevice);

    cudaMemset((void*)distCur, 0, distanceVectorSize);
    cudaMemcpy((void*)distCur, &ZERO, sizeof(int), cudaMemcpyHostToDevice);
    for(int i = 1; i < numVertices; i++){
        cudaMemcpy((void*)&distCur[i], &INF, sizeof(int), cudaMemcpyHostToDevice);
    }

    cudaMemset((void*)distPrev, 0, distanceVectorSize);
    cudaMemcpy((void*)distPrev, &ZERO, sizeof(int), cudaMemcpyHostToDevice);
    for(int i = 1; i < numVertices; i++){
        cudaMemcpy((void*)&distPrev[i], &INF, sizeof(int), cudaMemcpyHostToDevice);
    }

    // launch kernels
    while(true){
        outcoreKernel<<<blockNum, blockSize>>>(edgesDev, len, distPrev, distCur, hasUpdated);
        //printf("%d\n", readCudaInt(len));
        filterEdges<<<1, 1>>>(edgesDev, len, edgesDev2, distPrev, distCur);
        printf("%d\n", readCudaInt(len));
        break;
        if(!readCudaInt(hasUpdated)) break;
        swap((void**)&distCur, (void**)&distPrev);
        swap((void**)&edgesDev, (void**)&edgesDev2);
    }

    // copy output from device
    int *output = (int*) malloc(distanceVectorSize);
    cudaMemcpy((void*)output, distCur, distanceVectorSize, cudaMemcpyDeviceToHost);

    writeAnswer(output, numVertices);

}

void neighborHandler(std::vector<edge> * edgesPtr, int blockSize, int blockNum){
    setTime();

    if(!((blockSize * blockNum < 2048) && (blockNum < 64))){
        puts("ERROR: blockNum must be <= 64 and total threads must be <= 2048\n");
        return;
    }
    std::vector<edge> edges = *edgesPtr;
    std::sort(edges.begin(), edges.end(), edgeSrcComparator);
    neighborHandlerHost(edges, blockSize, blockNum);

    cudaDeviceProp props; cudaGetDeviceProperties(&props, 0);
    printf("The total computation kernel time on GPU %s is %f milli-seconds\n", props.name, getTime());
}
