#include<stdio.h>
#include<string.h>
#include "encode.h"

int main(int argc, char const *argv[])
{
    switch(argc)
    {
        case 6:
            if(strcmp(argv[1],"-E") == 0)
            {
                int flag=0;
                if(strcmp(argv[2],"-i")!=0)
                    flag = 1;
                if(strcmp(argv[4],"-s")!=0)
                    flag=1;
                if(flag == 0)
                {
                    encode(argv[3],argv[5]);
                }
            }
    }
    return 0;
}
