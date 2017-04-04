#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>

typedef struct Node{
    int value;
    struct Node *next;
} Node;

int valid_file(FILE *);
Node* insert(Node*, int);
void print_list(Node*);
Node* delete(Node*, int);
Node* destroy_list(Node*);

int main(int argc, char *argv[]){
    /* make sure an arguement is provided */
    if(argc != 2){
        printf("error");
        return 0;
    }

    /* Inspect the file */
    FILE *f = fopen(argv[1], "r"); //open the file
    if(!valid_file(f)){ //validate file
        printf("error");
        return 0;
    }
    char line[100]; //holds a line from the input file
    if(!fgets(line, 100, f)){ //check if file is empty
        printf("\n");
        return 0;
    }
    rewind(f);//restart read stream from beginning
    
    Node *front = NULL; //create head of linked list
   
    /* iterate through text file */
    while(fgets(line, 100, f)){
        //segment line into operation and number 
        char *operation = strtok(line, "\t");
        int num = atoi(strtok(NULL, "\t"));
        //perform the operation
        front = (*operation == 'i') ? insert(front, num) : delete(front, num);
    }
    
    print_list(front); //print the list

    //Cleanup
    front = destroy_list(front);//destroy the list
    fclose(f); //close file stream

    return 0;
}

/* Validates the structure of the input file */
int valid_file(FILE *f){
    /* check if file exists */
    if(!f){
        return 0;
    }

    /* Check if file has proper format */
    char line[100];
    while(fgets(line, 100, f)){
        char *line_segment = strtok(line, "\t");

        //validate the resulting tokens
        if(!line_segment){ 
            return 0;
        }
        else if(!(strcmp(line_segment, "i") == 0 || strcmp(line_segment, "d") == 0)){
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
                        if(line_segment[i] == '-'){ //negative number
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
    }
    rewind(f); //set file read stream back to beginning

    return 1;
}

/* inserts a node, if it is not a duplicate, and returns the head of the linked list */
Node* insert(Node* front, int num){
    Node *curr = front;
    Node *prev = NULL;

    if(!front){ //empty list
        front = (Node*) malloc(sizeof(Node));
        front->value = num;
        front->next = NULL;
        return front;
    }

    //Stop when node has a value >= target
    while(curr->next != NULL && curr->value < num){
        prev = curr;
        curr = curr->next;
    }
    
    /* There can be no duplicates so we ignore the case where
     * the node we stop on is equal to the node to be inserted*/
    if(curr->value < num){
        Node *temp_next = curr->next;
        curr->next = (Node*) malloc(sizeof(Node));
        (curr->next)->value = num;
        (curr->next)->next = temp_next;
    }
    else if(curr->value > num){
        if(prev == NULL){
            Node *new_front = (Node*) malloc(sizeof(Node));
            new_front->next = front;
            new_front->value = num;
            front = new_front;
        }
        else{
            Node *added = (Node*) malloc(sizeof(Node));
            added->next = prev->next;
            added->value = num;
            prev->next = added;
        }
    }

    return front;
}

/* Prints the linked list to stdout */
void print_list(Node* front){ 
    if(!front){ //empty list
        printf("\n");
    }

    Node *ptr = front;
    while(ptr != NULL){
        printf("%i", ptr->value);
        if(ptr->next){
            printf("\t");
        }
        ptr = ptr->next;
    }

    return;
}

/* Deletes the node with the target value and returns the head*/
Node* delete(Node *front, int num){
    Node *ptr = front;
    Node *prev = NULL;

    //search for target
    while(ptr !=NULL && ptr->value != num){
        prev = ptr;
        ptr = ptr->next;
    }

    if(ptr == NULL){ //could not find the target or empty list
        return front;
    }
    else if(prev == NULL){ //target is at the front
        prev = front;
        front = front->next;
        free(prev);
    }
    else{
        Node* temp = ptr;
        prev->next = ptr->next;
        free(temp);
    }
    
    return front;
}

/* frees everything malloc'd for the linked list*/
Node* destroy_list(Node *front){
    if(!front){ //empty list
        return front;
    }

    Node *prev = NULL;
    Node *ptr = front;

    while(ptr){
        if(ptr->next){
            prev = ptr;
            ptr = ptr->next;
            
            //destroy current node
            free(prev);
            prev = NULL;
        }
        else{
            free(ptr);
            ptr = NULL;
        }
    }

    return ptr;
}
