RM = rm -f
CC = gcc
CFLAGS = -c -std=gnu99

HINFOSVC: main.o
	$(CC) main.o -lm -o hinfosvc

clean:
	$(RM) *.o $(objects) hinfosvc
