all: test0 test1 test2 test3 test4

test0: test0.c mymalloc.o
	gcc -g -Wall test0.c mymalloc.o -o test0

test1: test1.c mymalloc.o
	gcc -g -Wall test1.c mymalloc.o -o test1

test2: test2.c mymalloc.o
	gcc -g -Wall test2.c mymalloc.o -o test2

test3: test3.c mymalloc.o
	gcc -g -Wall test3.c mymalloc.o -o test3

test4: test4.c mymalloc.o
	gcc -g -Wall test4.c mymalloc.o -o test4

mymalloc.o: mymalloc.c mymalloc.h
	gcc -g -Wall -c mymalloc.c

clean:
	rm -f *.o test0 test1 test2 test3 test4 *.gch
