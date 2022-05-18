CC=gcc
CFLAGS=-c -Wall -D_GNU_SOURCE
LDFLAGS=-lpcap

.PHONY: all sniffer.c ipk-sniffer

all: sniffer.c ipk-sniffer

ipk-sniffer: sniffer.o
	$(CC) $^ -o $@ $(LDFLAGS)

.c.o:
	$(CC) $(CFLAGS) -I. $< -o $@

clean:
	rm -rf sniffer.o ipk-sniffer