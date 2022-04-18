#include <stdio.h>
#include <stdlib.h>
#include <fcntl.h>
#include <limits.h>
#include <string.h>

int control(int NE, int NR, int TE, int TR){
    if(NE <= 0 || NE >= 1000){
        return 1;
    }
    else if(NR <= 0 || NR >= 20){
        return 1;
    }
    else if(TE <=0 || TE >=1000){
        return 1;
    }
    else if(TR <=0 || TR >=1000){
        return 1;
    }
    else{
        return 0;
    }
}

int main(int argc, char *argv[]){

    if(argc != 5 ){
        return 1;
    }
    int NE = atoi(argv[1]);
    int NR = atoi(argv[2]);
    int TE = atoi(argv[3]);
    int TR = atoi(argv[4]);
    if(control(NE, NR, TE, TR) == 1){
        return 1;
    }
    
    int id1 = fork();
    printf("%d", NE);


    return 0;
}