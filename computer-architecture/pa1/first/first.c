#include<stdio.h>
#include<stdlib.h>

int main(int argc, char *argv[]){ 
    //Check if user inputted an arguement
    if(argc != 2){
        printf("error\n");
        return 0;
    }
    
    //determine if number is even or odd
    char *result = (atoi(argv[1]) % 2 == 0) ? "even\n" : "odd\n";
    printf("%s", result);
    
    return 0;
}
