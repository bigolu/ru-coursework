/**************************** includes **************************/
#include <stdio.h>
#include <stdlib.h>
#include <string.h>


/**************************** typedefs ***************************/
typedef struct Node
{
    long data;
    struct Node *next;
} Node;


/***************************** function signatures ****************/
int hash(long);
void insert(Node**, long);
int size(Node**);
void destroy_hashtable(Node**);


/***************************** main ******************************/
int main(int argc, char** argv)
{
    // make sure an argument is provided 
    if(argc != 2){
        printf("error");
        return 0;
    }

    FILE* f = fopen(argv[1], "r"); //open the file and store it in f

    // see if file exists 
    if(!f){
        printf("error");
        return 0;
    }
    
    char* line = NULL; //holds a line from the input file; getline will allocate space for it
    size_t bytes = 0; //holds size in bytes of memory pointed to by line

    //check for empty
    int bytes_read = (int) getline(&line, &bytes, f);
    if(bytes_read == -1){
        printf("0");
        return 0;
    }
    rewind(f); //set file stream back to begginning

    Node** hashtable = (Node**) calloc(1000, sizeof(Node*)); //create hashtable

    while(getline(&line, &bytes, f) != -1){
        //TODO: see if i should check for valid lines
        
        long data = strtol(line, NULL, 0); // convert hexadecimal to integer

        insert(hashtable, data); 
    }
    
    printf("%d", size(hashtable)); //print size

    //cleanup
    destroy_hashtable(hashtable); //gotta free that space dawg
    free(hashtable);
    free(line);
    fclose(f);

    return 0;
}


/****************************** other functions *********************/
int hash(long l){
    return abs( (int) l % 1000 );
}

void insert(Node** hashtable, long data){
    int index = hash(data); //hash the data to get index to insert at
    
    if(!hashtable[index]){ 
       hashtable[index] = (Node*) malloc(sizeof(Node));
       (hashtable[index])->data = data;
       (hashtable[index])->next = NULL;
       return;
    }
    
    Node* ptr = hashtable[index];
    while(ptr->next != NULL){
        if(ptr->data == data){ //duplicate
            return;
        }
        ptr = ptr->next;
    }
    if(ptr->data == data){ //duplicate
        return;
    }

    //add node containing data to end of chain
    ptr->next = (Node*) malloc(sizeof(Node));
    ptr = ptr->next;
    ptr->data = data;
    ptr->next = NULL;

    return;
}

int size(Node** hashtable){
    int size = 0;

    int index = 0;
    for(; index < 1000; index++){
        Node* ptr = hashtable[index];
        while(ptr != NULL){
            size++;
            ptr = ptr->next;
        }
    }

    return size;
}

void destroy_hashtable(Node** hashtable){
    int index = 0;
    for(; index < 1000; index++){
        Node* ptr = hashtable[index];
        while(ptr != NULL){
            Node* tmp = ptr;
            ptr = ptr->next;
            free(tmp);
        }
    }
}
