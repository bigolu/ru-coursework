#include <vector>
#include <iostream>
#include <algorithm>

#include "utils.h"
#include "cuda_error_check.cuh"
#include "parse_graph.hpp"
#include "graph.h"
#include "limits.h"

__global__ void incoreKernel(edge *edges, int *len, int *distance, int *hasUpdated){
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
        int tmpDist = distance[src] + weight;
        if(distance[src] == INT_MAX) continue;
        if(tmpDist < distance[dest]){
            atomicMin(&distance[dest], tmpDist);
            *hasUpdated = 1;
        }
    }
}

void outcoreHost(std::vector<edge> edges, int blockSize, int blockNum){
    int numVertices = getNumVertices(edges);
    int numEdges = edges.size();
    int distanceVectorSize = sizeof(int) * numVertices;

    // init device args
    edge *edgesDev; int *len; int *distCur; int *distPrev; int *hasUpdated;
    cudaMalloc((void**)&edgesDev, numEdges * sizeof(edge));
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

    // launch device kernel
    while(true){
        outcoreKernel<<<blockNum, blockSize>>>(edgesDev, len, distPrev, distCur, hasUpdated);
        if(!readCudaInt(hasUpdated)) break;
        swap((void**)&distCur, (void**)&distPrev);
    }
    
    // copy output from device
    int *output = (int*) malloc(distanceVectorSize);
    cudaMemcpy((void*)output, distCur, distanceVectorSize, cudaMemcpyDeviceToHost);

    writeAnswer(output, numVertices);
}

void incoreHost(std::vector<edge> edges, int blockSize, int blockNum){
    int numVertices = getNumVertices(edges);
    int numEdges = edges.size();
    int distanceVectorSize = sizeof(int) * numVertices;

    // init device args
    edge *edgesDev; int *len; int *distance; int *hasUpdated;
    cudaMalloc((void**)&edgesDev, numEdges * sizeof(edge));
    cudaMalloc((void**)&len, sizeof(int));
    cudaMalloc((void**)&distance, distanceVectorSize);
    cudaMalloc((void**)&hasUpdated, sizeof(int));

    cudaMemcpy(edgesDev, edges.data(), edges.size()*sizeof(edge), cudaMemcpyHostToDevice);
    cudaMemcpy(len, &numEdges, sizeof(int), cudaMemcpyHostToDevice);
    cudaMemcpy((void*)hasUpdated, &ZERO, sizeof(int), cudaMemcpyHostToDevice);

    cudaMemset((void*)distance, 0, distanceVectorSize);
    cudaMemcpy((void*)distance, &ZERO, sizeof(int), cudaMemcpyHostToDevice);
    for(int i = 1; i < numVertices; i++){
        cudaMemcpy((void*)&distance[i], &INF, sizeof(int), cudaMemcpyHostToDevice);
    }

    // launch device kernel
    while(true){
        incoreKernel<<<blockNum, blockSize>>>(edgesDev, len, distance, hasUpdated);
        if(!readCudaInt(hasUpdated)) break;
    }
    
    // copy output from device
    int *output = (int*) malloc(distanceVectorSize);
    cudaMemcpy((void*)output, distance, distanceVectorSize, cudaMemcpyDeviceToHost);

    writeAnswer(output, numVertices);
}

void puller(std::vector<edge> *edgesPtr, int blockSize, int blockNum, int outcore){
    setTime();

    std::vector<edge> edges = *edgesPtr;
    std::sort(edges.begin(), edges.end(), edgeSrcComparator);
    outcore ? outcoreHost(edges, blockSize, blockNum) : incoreHost(edges, blockSize, blockNum);
    
    cudaDeviceProp props; cudaGetDeviceProperties(&props, 0);
    printf("The total computation kernel time on GPU %s is %f milli-seconds\n", props.name, getTime());
}
        /*int *tmp = (int*)malloc(vertexVectorSize); 
        int *tmc = (int*)malloc(vertexVectorSize);
        cudaMemcpy((void*)tmp, distPrev, vertexVectorSize, cudaMemcpyDeviceToHost);
        cudaMemcpy((void*)tmc, distCur, vertexVectorSize, cudaMemcpyDeviceToHost);
            
        for(int i = 0; i < numVertices; i++){
            printf("%d, ", tmp[i]);
        }
        printf("%s", "\n\n");
        for(int i = 0; i < numVertices; i++){
            printf("%d, ", tmc[i]);
        }*/
