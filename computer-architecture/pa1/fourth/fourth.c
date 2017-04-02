#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>

int valid_line(char*);
int hash(int, int);
int insert(int**, int, int);
int search(int**, int, int);
void destroy_hashmap(int**, int);

int main(int argc, char *argv[])
{
    /* make sure an arguement is provided */
    if(argc != 2){
        printf("error");
        return 0;
    }

    /* Inspect the file */
    FILE *f = fopen(argv[1], "r"); //open the file
    if(!f){ //check if file exists
        printf("error");
        return 0;
    }
    char line[100]; //holds a line from the input file
    if(!fgets(line, 100, f)){ //check if file is empty
        printf("\n");
        return 0;
    }
    rewind(f);//restart read stream from beginning

    /* initialize hashmap */
    int *hashmap[1000];
    int length = sizeof(hashmap) / sizeof(hashmap[0]); //get size of array
    int i = 0;
    for(; i < length; i++){
        hashmap[i] = NULL;
    }
    
    /* iterate through text file */
    while(fgets(line, 100, f)){
        if(!valid_line(line)){
            printf("error\n");
            continue;
        }

        //segment line into operation and number 
        char *operation = strtok(line, "\t");
        int num = atoi(strtok(NULL, "\t"));

        if(*operation == 'i'){ // insert
            if(!search(hashmap, num, length)){
                insert(hashmap, num, length);
                printf("inserted\n");
            }
            else{
                printf("duplicate\n");
            }
        }
        else{ //search
            char* str = (search(hashmap, num, length)) ? "present\n" : "absent\n";
            printf("%s", str);
        }
    }
    
    destroy_hashmap(hashmap, length); //free malloc'd space for hashmap

    return 0;
}



/* Validates the structure of the input line */
int valid_line(char* line){
    //make copy of line
    char *copy = (char*) malloc(sizeof(char) * 100);
    copy = strcpy(copy, line);

    char *line_segment = strtok(copy, "\t");

    //validate the resulting tokens
    if(!line_segment){ 
        return 0;
    }
    else if(!(strcmp(line_segment, "i") == 0 || strcmp(line_segment, "s") == 0)){
        return 0;
    }
    line_segment = strtok(NULL, "\t"); //go to next token
    if(!line_segment){ 
        return 0;
    }
    else{
        int i = 0;
        for(; i < strlen(line_segment) - 1; i++){
            if(!isdigit(line_segment[i])){
                if(i == 0){
                    if(line_segment[i] == '-'){
                        continue;
                    }
                }
                return 0;
            }
        }
    }
    line_segment = strtok(NULL, "\t");
    if(line_segment){
        return 0;
    }

    return 1;
}

/* generates a hashcode for an element to be inserted */
int hash(int num, int length){
    if(num < 0){
        return abs(num) % length; // negative lul
    }
    return (num % length); //lul
}                  

/* inserts an item. returns 0 if unsuccessful and 1 otherwise */
int insert(int** hashmap, int num, int length){
    int index = hash(num, length); //hash the data
    
    if(!hashmap[index]){
        hashmap[index] = (int*) malloc(sizeof(int));
        *(hashmap[index]) = num;
        return 1;
    }
    else{ //I must probe....linearly
        for(; index < length; index++){ // look for free spot after the attempted index
            if(!hashmap[index]){
                hashmap[index] = (int*) malloc(sizeof(int));
                *(hashmap[index]) = num;
                return 1;
            }
        }
        int original_index = index;
        index = 0;
        for(; index < original_index; index++){ // look for free spot before the attempted index
            if(!hashmap[index]){
                hashmap[index] = (int*) malloc(sizeof(int));
                *(hashmap[index]) = num;
                return 1;
            }
        }
    }
    
    return 0; 
}

/* searches for an item. if found returns 1 and returns 0 otherwise */
int search(int** hashmap, int num, int length){
    int index = 0;
    for(; index < length; index++){
        if(hashmap[index]){
            if(*(hashmap[index]) == num){
                return 1; //goteeem!
            }
        }
    }

    return 0; //was not found
}

void destroy_hashmap(int** hashmap, int length){
    int index = 0;
    for(; index < length; index++){
        //THIS COMMENTED BLOCK PRINTS THE CONTENTS OF AN INDEX TO STDOUT
        /*if(hashmap[index]){
            printf("******%i\n", *(hashmap[index]));
        }
        else{
            printf("EMPTY\n");
        }*/

        free(hashmap[index]);
        hashmap[index] = NULL;
    }

    return;
}
