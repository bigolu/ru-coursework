#include <vector>
#include <iostream>
#include <algorithm>

#include "utils.h"
#include "cuda_error_check.cuh"
#include "parse_graph.hpp"
#include "graph.h"
#include "limits.h"

__global__ void processEdges(edge *edges, int edgesLen, int *distPrev, int *distCur, int *hasUpdated){
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
        int dest = edges[i].dest;
        int weight = edges[i].weight;

        // avoid overflow when adding weight to src
        if(distPrev[src] == INT_MAX)
            continue;
        
        int dist = distPrev[src] + weight;
        if(dist < distPrev[dest]){
            atomicMin(&distCur[dest], dist);
            *hasUpdated = 1;
        }
    }
}

void bmfOutcore(std::vector<edge> edges, int blockSize, int blockNum){
    int numVertices = getNumVertices(edges);
    int numEdges = edges.size();
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

    // start BMF
    while(true){
        processEdges<<<blockNum, blockSize>>>(edges_d, numEdges, distPrev, distCur, hasUpdated);
        setToMin<<<blockNum, blockSize>>>(distPrev, distCur, numVertices);

        if(!readCudaInt(hasUpdated))
            break;

        // reset for next iteration
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
    free(output);
}

__global__ void processEdgesIncore(edge *edges, int edgesLen, int *dist, int *hasUpdated){
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
        int dest = edges[i].dest;
        int weight = edges[i].weight;

        // avoid overflow when adding weight to src
        if(dist[src] == INT_MAX)
            continue;
        
        int tmpDist = dist[src] + weight;
        if(tmpDist < dist[dest]){
            atomicMin(&dist[dest], tmpDist);
            *hasUpdated = 1;
        }
    }
}

void bmfIncore(std::vector<edge> edges, int blockSize, int blockNum){
    int numVertices = getNumVertices(edges);
    int numEdges = edges.size();
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

    // start BMF
    while(true){
        processEdgesIncore<<<blockNum, blockSize>>>(edges_d, numEdges, dist, hasUpdated);

        if(!readCudaInt(hasUpdated))
            break;

        // reset for next iteration
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
    free(output);
}

void puller(std::vector<edge> *edgesPtr, int blockSize, int blockNum, int outcore){
    setTime();

    std::vector<edge> edges = *edgesPtr;
    std::sort(edges.begin(), edges.end(), edgeSrcComparator);
    outcore ? bmfOutcore(edges, blockSize, blockNum) : bmfIncore(edges, blockSize, blockNum);
    
    cudaDeviceProp props; cudaGetDeviceProperties(&props, 0);
    printf("The total computation kernel time on GPU %s is %f milli-seconds\n", props.name, getTime());
}
