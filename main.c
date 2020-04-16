#include <stdio.h>

int main()
{
	FILE *readFile, *writeFile;
	readFile = fopen("results.txt", "r");
	writeFile = fopen("results.csv", "w");

	int thisChar = fgetc(readFile);
	while(thisChar != EOF)
	{
		if (thisChar == ';')
		{
			fputc('\n', writeFile);
		}
		else if (thisChar == ',')
		{
			fputc(',', writeFile);
		}
		else if (thisChar == '.')
		{
			fputc('.', writeFile);
		}
		else if ((thisChar >= '0') && (thisChar <= '9'))
		{
			fputc(thisChar, writeFile);
		}
		thisChar = fgetc(readFile);
	}	
	fclose(readFile);
	fclose(writeFile);
	return 0;
}
