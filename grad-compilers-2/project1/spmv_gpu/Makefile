#For this file, I owe my soul to:
#https://devblogs.nvidia.com/parallelforall/separate-compilation-linking-cuda-device-code

CC = /usr/local/cuda-7.5/bin/nvcc
GCC = g++
objects = mmio.o main.o genresult.o

all: $(objects) 
	$(CC) -g -arch=sm_30 $(objects) -o spmv

mmio.o: mmio.c
	$(GCC) -g -w mmio.c -c

main.o: main.c
	$(CC) -g -x cu -arch=sm_30 -I. -dc main.c -o $@

genresult.o: genresult.c spmv_atomic.c spmv_segment.c spmv_design.c
	$(CC) -g -x cu -arch=sm_30 -I. -dc genresult.c -o $@

%.o: %.c
	$(CC) -g -x cu -arch=sm_30 -I. -dc $< -o $@

clean:
	rm -f *.o spmv output.txt

#Quick conveniences for submission etc.
tar:
	tar -cvf spmv_proj.tar *.c *.cuh *.pdf makefile

untar:
	tar -xvf spmv_proj.tar
