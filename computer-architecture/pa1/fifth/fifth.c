#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>

int main(int argc, char *argv[])
{
    /* make sure an arguement is provided */
    if(argc != 2){
        printf("error\n");
        return 0;
    }

    /* Inspect the file */
    FILE *f = fopen(argv[1], "r"); //open the file
    if(!f){ //check if file exists
        printf("error\n");
        return 0;
    }
    char temp[100]; //holds a line from the input file
    if(!fgets(temp, 100, f)){ //check if file is empty
        printf("\n");
        return 0;
    }
    
    //get rows and columns
    int rows = atoi(strtok(temp, "\t"));
    int cols = atoi(strtok(NULL, "\t"));

    if(rows == 0 && cols == 0){ // cuz ya never know
        printf("\n");
    }

    char* line = malloc(sizeof(char) * (cols + 20)); //holds a line from the input file

    /* create 2d array for resulting matrix */
    int** matrix = malloc(sizeof(int*) * rows);
    int i = 0;
    for(; i < rows; i++){
        matrix[i] = malloc(sizeof(int) * cols);
    }
    //set all spaces to 0
    int k = 0;
    i = 0;
    for(; i < rows; i++){
        k = 0;
        for(; k < cols; k++){
            (matrix[i])[k] = 0;
        }
    }

    /* iterate through text file */
    k = 0;
    for(; k < 2; k++){ //runs for each matrix
        int rownum = 0;
        for(; rownum < rows; rownum++){ //iterates through matrix
            fgets(line, 100, f); // move to next line
            int num = atoi(strtok(line, "\t"));
            (matrix[rownum])[0] += num;//first column

            i = 1;
            for(; i < cols; i++){ //all subsequent columns
                num = atoi(strtok(NULL, "\t"));
                (matrix[rownum])[i] += num;
            }
        }
        fgets(line, 100, f); //skip blank line
    }

    /* this block prints the matrix to stdout */
    i = 0;
    for(; i < rows; i++){
        k = 0;
        for(; k < cols; k++){
            printf("%i\t", (matrix[i])[k]);
        }
        printf("\n");
    }

    //cleanup
    free(line);
    i = 0;
    for(; i < rows; i++){
        free(matrix[i]);
    }
    free(matrix);

    return 0;
}
