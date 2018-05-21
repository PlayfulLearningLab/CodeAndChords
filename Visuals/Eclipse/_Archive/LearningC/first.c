/*
 * first.c
 *
 *  Created on: Jun 27, 2017
 *      Author: meue0006
 */

#include <stdio.h>
#include <portaudio_x86.lib>

int main()
{
	int	x;
	int	*pointer;
	int	pos;

	char	name[50];

	printf("Please enter your name: ");
	fflush(stdout);
	fgets(name, 50, stdin);

	pointer	= &x;
//	scanf("%d", &x);

	pos	= (int)pointer;

	printf("The value of x (%d) is stored in memory location %d.", *pointer, pos);
	return(0);
} // main
