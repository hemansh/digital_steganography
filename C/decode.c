#include<string.h>
#include<stdio.h>
#include"decode.h"

int get_size(FILE *ptr)
{
    int buffer=0,i;
    int tmp,msg;
    for(i=0;i<8;i++)
    {
        tmp = fgetc(ptr);
        msg = (tmp & 1);
        if(msg)
        {
            buffer = (buffer << 1) | 1;
        }
        else
        {
            buffer = buffer << 1;
        }
    }
    return buffer;
}

void string_decryption(FILE *pf1,char *strng,int size)
{
	int file_buff=0, i, j=0, k=0;
	int ch, bit_msg;
	for (i = 0; i < (size * 8); i++)
	{
		j++;
		ch = fgetc(pf1);
		bit_msg = (ch & 1);
		if (bit_msg)
		{
			file_buff = (file_buff << 1) | 1;
		}
		else
		{
			file_buff = file_buff << 1;
		}

		if ( j == 8)
		{
			strng[k] =(char)file_buff; 
			j=0;
			k++;
			file_buff = 0;
		}
	}
	strng[k] = '\0';
}

void secret_decryption(int size_txt, FILE *pf1)
{
	int file_buff=0, i, j = 0, k = 0;
	int ch,bit_msg;
	char output[250] = {0};
	for (i = 0; i < (size_txt * 8); i++)
	{
		j++;
		ch = fgetc(pf1);
		bit_msg = (ch & 1);
		if (bit_msg)
		{
			file_buff = (file_buff << 1) | 1;
		}
		else
		{
			file_buff = file_buff << 1;
		}

		if ( j == 8)
		{
			//putc(file_buff, pf2);
			output[k++] = file_buff;
			j=0;
			file_buff = 0;
		}
	}
	printf("\n*** Secret Text Is ==> %s\n\n", output);
}

int decode(const char *str)
{
    FILE *ptr;

    if((ptr = fopen(str,"r"))==NULL)
    {
        printf("error opening image\n");
        return 1;
    }

    fseek(ptr,54,SEEK_SET);

    char magic_str_user[20],magic_str_image[20];
    int i;

    int magic_size = get_size(ptr);
    string_decryption(ptr,magic_str_image,magic_size);

    printf("enter magic string:");
    for(i=0;(magic_str_user[i] = getchar()) != '\n';i++);
    magic_str_user[i]='\0';

    if(strcmp(magic_str_user,magic_str_image) == 0)
    {
        printf("magic string correct\n");
    }
    else
    {
        printf("wrong magic string\n");
        return 0;
    }

    char passwd_user[20],passwd_image[20];
    int passwd_size = get_size(ptr);
    string_decryption(ptr,passwd_image,passwd_size);

    printf("Enter password:");
    scanf("%s",passwd_user);

    if(strcmp(passwd_image,passwd_user)==0)
    {
        printf("Password correct");
    }
    else
    {
        printf("Wrong password\n");
        return 0;
    }

    int msg_size = get_size(ptr);
    secret_decryption(msg_size,ptr);

    fclose(ptr);
    
}