#include <stdio.h>
#include <stdlib.h>
#include <math.h>

int main(int argc, char *argv[]){ 
    //check if user inputted an arguement
    if(argc != 2){
        printf("error");
        return 0;
    }

    int num = atoi(argv[1]);

    //determine if num is prime
    if(num == 1){ //special case
        printf("no");
    }
    else if(num == 2 || num == 3 || num == 5 || num ==7){ //more special cases
        printf("yes");
    }
    else{
        int stop = num -1;
        int i = 2;
        for(; i < stop; i++){
            if( num % i == 0){
                printf("no");
                return 0;
            }
        }
        
        printf("yes");
    }
 
    return 0;
}
