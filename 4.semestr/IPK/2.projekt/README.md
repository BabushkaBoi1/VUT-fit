# IPK - projekt 2 - Varianta ZETA: Sniffer paketů
### atuhor: Hájek Vojtěch (xhajek51)

## Překlad a spuštění programu
Pro přeložení programu je zde implementován soubor Makefile. Pro jeho spuštění zadejte do terminálu make, či make all
> $ make \
> $ make all

Pokud chcete přeložené soubory odstranit, stačí zadat make clean
> $ make clean

Pro samotné spuštění programu, zadejte do terminálu:
> $ ./ipk-sniffer

Přepínače programu:
> $ ./ipk-sniffer [-i rozhraní | --interface rozhraní] {-p port} {[--tcp|-t] [--udp|-u] [--arp] [--icmp] } {-n num}

Program je možno ukončit stisknutím CTRL+C. \
Pro vypsání nápovědy v programu stačí přidat parametr help:
> $ ./ipk-sniffer -h \
> $ ./ipk-sniffer --help

## Implementace
Implementace programu by měla odpovídat zadání, nebylo zde implementováno nějaké rozšíření. 

Pokud uživatel spustí program bez parametru -i, či bude parametr uveden bez hodnoty, jsou vypsány všechny dostupné zařízení:
> $ ./ipk-sniffer  | ./ipk-sniffer -i | ./ipk-sniffer --interface \
> ens33 \
> lo \
> ...

Příklad výstupu programu:
```console
timestamp: 2022-04-24T-11:29:26.581949Z
src MAC: 00:0C:29:9A:CD:7D
dst MAC: 00:50:56:FC:5C:00
frame lenght: 74 bytes
src IP: 192.168.251.128
dst IP: 93.184.216.34
src port: 45546
dst port: 11740

0x0000:    00 50 56 FC 5C 00 00 0C 29 9A CD 7D 08 00 45 00         .PV.\...)..}..E.
0x0010:    00 3C C3 7E 40 00 40 06 85 39 C0 A8 FB 80 5D B8         .<.~@.@..9...�].
0x0020:    D8 22 B1 EA 2D DC F1 13 4E 9A 00 00 00 00 A0 02         ."..-...N.......
0x0030:    FA F0 F2 32 00 00 02 04 05 B4 04 02 08 0A 19 3D         ...2...........=
0x0040:    51 C7 00 00 00 00 01 03 03 07                           Q.........
```

## Seznam odevzdaných souborů
- sniffer.c
- README.md
- manual.pdf
- Makefile