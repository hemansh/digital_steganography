#include<stdio.h>
#include<string.h>
#include<stdlib.h>
#include "encode.h"

int image_size(FILE *ptr)
{
    int w,h;
    fseek(ptr,0x12,SEEK_SET);
    fread(&w,sizeof(int),1,ptr);
    fread(&h,sizeof(int),1,ptr);
    printf("Dimention of image is %d x %d\n",w,h);
    fseek(ptr,0L,SEEK_SET);
    return (w * h * 3)/8;
}

int secret_msg_size(FILE *ptr)
{
    int size;
    fseek(ptr,0L,SEEK_END);
    size = ftell(ptr);
    fseek(ptr,0L,SEEK_SET);
    return size;
}

void secret_text(FILE *ptr)
{
	char ch;
	while ((ch = getchar())!='\n')
	{
		putc(ch,ptr);
	}
}

int get_bit(char byte, int bit)
{
	return ((byte >> 8 - bit) & 1);
}


void hide_size(int num,FILE *fp1, FILE *fp3)
{

	char file_buff;	
	int i, j = 0;
	int bit_msg;

	for(i = 1; i <= 8; i++)
	{
		file_buff = fgetc(fp1);

		int file_byte_lsb = (file_buff & 1);

		bit_msg = get_bit(num, i);

		if(file_byte_lsb == bit_msg)
		{
			fputc(file_buff, fp3);
		}
		else
		{
			if(file_byte_lsb == 0)
				file_buff = (file_buff | 1);
			else
				file_buff = (file_buff ^ 1);

			fputc(file_buff, fp3);
		}
	}

}

void hide_str(char *str,FILE *fp1, FILE *fp3)
{

	char file_buff, msg_buff;
	int i, j = 0;
	int bit_msg;
	while((msg_buff = str[j]) != '\0')
	{
		for(i = 1; i <= 8; i++)
		{
			file_buff = fgetc(fp1);

			int file_byte_lsb = (file_buff & 1);

			bit_msg = get_bit(msg_buff, i);

			if(file_byte_lsb == bit_msg)
			{
				fputc(file_buff, fp3);
			}
			else
			{
				if(file_byte_lsb == 0)
					file_buff = (file_buff | 1);
				else
					file_buff = (file_buff ^ 1);

				fputc(file_buff, fp3);
			}
		}
		j++;
	}
}

void hide_msg(FILE *fp1, FILE *fp2, FILE *fp3)
{
	char file_buff = 0, msg_buff = 0, ch;
	int i;
	int bit_msg;
	while((msg_buff = fgetc(fp2)) != EOF)
	{
		for(i = 1; i <= 8; i++)
		{
			file_buff = fgetc(fp1);

			int file_byte_lsb = (file_buff & 1);

			bit_msg = get_bit(msg_buff, i);

			if(file_byte_lsb == bit_msg)
			{
				fputc(file_buff, fp3);
			}
			else
			{
				if(file_byte_lsb == 0)
					file_buff = (file_buff | 1);
				else
					file_buff = (file_buff ^ 1);

				fputc(file_buff, fp3);
			}
		}
	}

	while(!feof(fp1))
	{
		char tmp_cpy = fgetc(fp1);
		fputc(tmp_cpy,fp3);

	}

	if(msg_buff == EOF)
		printf("\n*** Secret Message Encrypted Successfully ***\n");
	else
		printf("\n*** Failed Encrypting ***\n");
}

int encode(const char* pram1,const char *pram2)
{
    FILE *img,*secret_msg, *img_out;
    if((img = fopen(pram1,"r+"))==NULL)
    {
        printf("Error opening Image\n");
        return 1;
    }
    int size_image = image_size(img);

    secret_msg = fopen(pram2,"w+");
    printf("Enter your secret text:");
    secret_text(secret_msg);
    int secret_size = secret_msg_size(secret_msg);
    printf("secret msg size:%d",secret_size);

    if(secret_size > size_image)
    {
        printf("Size of message exceeds the image size\n");
        return 1;
    }

    img_out = fopen("out_image.bmp","w+");
    if(img_out == NULL)
    {
        fprintf(stderr,"Cannot create output file\n");
        exit(1);
    }

    int i,c=0;
    char tmp;
    rewind(img);
    for(i=0;i<54;i++)
    {
        tmp = getc(img);
        fputc(tmp,img_out);
        c++;
    }

    if(i == c)
    {
        printf("\ncopying header sucessful");
    }
    else
    {
        printf("\nerror copying header");
        return 0;
    }


    printf("enter hash and doller only:");
    char magic_str[10];
    char magic=getchar();
    
    for(i=0;magic != '\n';i++)
    {
        if(magic == '#' || magic == '$')
        {
            magic_str[i]=magic;
        }
        else
        {
            printf("%i",magic);
            printf("entered wrong char\n");
            exit(2);
        }
        magic=getchar();
    }
    magic_str[i]='\0';
    int magic_size = strlen(magic_str);
    hide_size(magic_size,img,img_out);
    hide_str(magic_str,img,img_out);

    char passwd[20];
    printf("Enter password:");
    for(int i=0;((passwd[i]=getchar())!= '\n');i++);
    passwd[i]='\0';
    int passwd_size = strlen(passwd);
    hide_size(passwd_size,img,img_out);
    hide_str(passwd,img,img_out);

    hide_size(secret_size,img,img_out);
    hide_msg(img,secret_msg,img_out);

    fclose(img);
    fclose(img_out);
    fclose(secret_msg);

    return 0;
}