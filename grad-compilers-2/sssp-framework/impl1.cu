#include <vector>
#include <iostream>
#include <algorithm>

#include "utils.h"
#include "cuda_error_check.cuh"
#include "parse_graph.hpp"
#include "graph.h"

__global__ void pulling_kernel(std::vector<edge> * edges, std::vector<int>
    distances, int * hasChanged){

    //update me based on my neighbors. Toggle anyChange as needed.
    //offset will tell you who I am.
}

bool edgeSrcComparator(edge a, edge b){ return (a.src < b.src); }
bool edgeDestComparator(edge a, edge b){ return (a.dest < b.dest); }

void printEdges(std::vector<edge> edges){
    for(edge e : edges){
        printf("src: %d, dst: %d, weight: %d\n", e.src, e.dest, e.weight);
    }
}

int getNumVertices(std::vector<edge> edges){
    int max = -1;
    for(edge e : edges){
        max = std::max(max, std::max(e.src, e.dest));
    }

    return max + 1;
}

void puller(std::vector<edge> *edgesPtr, int blockSize, int blockNum){
    setTime();

    /*
     * Do all the things here!
     **/
    std::vector<edge> edges = *edgesPtr;
    std::sort(edges.begin(), edges.end(), edgeSrcComparator);

    cudaDeviceProp props; cudaGetDeviceProperties(&props, 0);
    int numVertices = getNumVertices(edges);
    int vertexVectorSize = Sizeof(int) * numVertices;
    int *output = (int*) malloc(vertexVectorSize);
    int *distCur; int *distPrev; bool *hasUpdated;
    cudaMalloc((void**)&distCur, vertexVectorSize);
    cudaMalloc((void**)&distPrev, vertexVectorSize);
    cudaMalloc((void**)&hasUpdated, vertexVectorSize);


    printf("The total computation kernel time on GPU %s is %f milli-seconds", props.name, getTime());
}
