#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <math.h>
#include <float.h>

#define size_row 1000
#define size_col 100

int control(int argc, char **argv, char *delim)
{
    //kontrola vstupu
    //uzivatel musi zadat minimalne 2 argumenty
    if (argc >= 2)
    {
        //uzivatel rozdeli sloupce pomoci delim
        if (strcmp(argv[1], "-d") == 0)
        {
            *delim = argv[2][0];
        }
        //kdyby uzivatel nezadal delim je neutralni znak mezera
        else
        {
            *delim = ' ';
        }
        return EXIT_SUCCESS;
    }
    else
    {
        fprintf(stderr, "ERR: nezadali jste zadny argument\n");
        return EXIT_FAILURE;
    }
}

void print(char (*table)[size_col], int columns)
{
    //vypsani tabulky radku
    int i = 0;
    while (i <= columns)
    {
        fprintf(stdout, "%s", table[i]);
        i++;
    }
    fprintf(stdout, "\n");

    return;
}

void irow(int col, int row, char *R, char delim)
{
    int r = atoi(R);

    //prida novy radek, pred zadany radek
    if (r == row)
    {

        for (int i = 0; i < col; i++)
        {
            fprintf(stdout, "%c", delim);
        }
        fprintf(stdout, "\n");
    }
    return;
}

void arow(int col, char delim)
{
    //přidává poslední řádek s daným počtem sloupců
    for (int i = 0; i < col; i++)
    {
        fprintf(stdout, "%c", delim);
    }

    fprintf(stdout, "\n");
    return;
}

int icol(char (*table)[size_col], char *C, char delim)
{
    //prida delim, tudiz sloupec pred sloupec c
    int c = atoi(C);
    if (c > 0)
    {
        c = c - 1;
        //pro prvni sloupec
        if (c == 0)
        {
            //vytvarim si pomocnou tabulku, kterou pak s delimem zkopiruju
            char hTable[101];
            hTable[0] = delim;
            strncat(hTable, table[c], 100);
            strcpy(table[c], hTable);
        }
        //pridavam delim na konec predchoziho sloupce
        else
        {
            strncat(table[c - 1], &delim, 1);
        }
        return EXIT_SUCCESS;
    }
    else
    {
        fprintf(stderr, "ERR: spatny argument");
        return EXIT_FAILURE;
    }
}

void acol(char (*table)[size_col], int col, char delim)
{
    //prida sloupec na konec, pred posledni sloupec se znakem \n
    strncat(table[col], &delim, 1);
    return;
}

int dcol(char (*table)[size_col], char *C)
{
    //vymaze obsah sloupce
    int c = atoi(C);
    if (c > 0)
    {
        memset(table[c - 1], 0, (int)sizeof(table[c - 1]));
        return EXIT_SUCCESS;
    }
    else
    {
        fprintf(stderr, "ERR: spatny argument");
        return EXIT_FAILURE;
    }
}

int dcols(char (*table)[size_col], char *N, char *M)
{
    int n = atoi(N);
    int m = atoi(M);
    if (n > 0 && m > 0)
    { //overeni zda je promena N mensi nebo rovno M
        if (n <= m)
        {
            //vymaze sloupce od N po M
            while (n <= m)
            {
                memset(table[n - 1], 0, (int)sizeof(table[n - 1]));
                n++;
            }
            return EXIT_SUCCESS;
        }
        else
        {
            fprintf(stderr, "ERR: prvni cislo musi byt mensi nez druhe");
            return EXIT_FAILURE;
        }
    }
    else
    {
        fprintf(stderr, "ERR: spatny argument");
        return EXIT_FAILURE;
    }
}

int cset(char (*table)[size_col], char *C, char *str, char delim)
{
    int c = atoi(C);
    if (c > 0)
    {
        c = c - 1;
        //pokud sloupec obsahuje delim, musi se tam po prepsani sloupce zase dopsat
        if (strchr(table[c], delim) != NULL)
        {
            strcpy(table[c], str);
            //prida delim na konec predchoziho sloupce
            strncat(table[c - 1], &delim, 1);
        }
        //sloupec se prepise stringem
        else
        {
            strcpy(table[c], str);
        }
        return EXIT_SUCCESS;
    }
    else
    {
        fprintf(stderr, "ERR: spatny argument");
        return EXIT_FAILURE;
    }
}

int toLower(char (*table)[size_col], char *C)
{
    int c = atoi(C);
    char alphaB[] = {"ABCDEFGHIJKLMNOPQRSTUVWXYZ"};
    char alphaS[] = {"abcdefghijklmnopqrstuvwxyz"};
    if (c > 0)
    {
        c = c - 1;
        //cyklus prochazi sloupec znak po znaku
        for (int i = 0; i < (int)sizeof(table[c]); i++)
        {
            for (int j = 0; j < (int)sizeof(alphaB); j++)
            {
                //kdyz se schoduje s nejakym velkym pismenem, prepise se na male pismeno
                if (table[c][i] == alphaB[j])
                {
                    table[c][i] = alphaS[j];
                }
            }
        }
        return EXIT_SUCCESS;
    }
    else
    {
        fprintf(stderr, "ERR: spatny argument");
        return EXIT_FAILURE;
    }
}

int toUpper(char (*table)[size_col], char *C)
{
    int c = atoi(C);
    char alphaB[] = {"ABCDEFGHIJKLMNOPQRSTUVWXYZ"};
    char alphaS[] = {"abcdefghijklmnopqrstuvwxyz"};
    if (c > 0)
    {
        c = c - 1;
        //stejny postup
        for (int i = 0; i < (int)sizeof(table[c]); i++)
        {
            for (int j = 0; j < (int)sizeof(alphaS); j++)
            {
                if (table[c][i] == alphaS[j])
                {
                    table[c][i] = alphaB[j];
                }
            }
        }
        return EXIT_SUCCESS;
    }
    else
    {
        fprintf(stderr, "ERR: spatny argument");
        return EXIT_FAILURE;
    }
}

int roundN(char (*table)[size_col], char *C, char delim)
{

    int c = atoi(C);
    if (c > 0)
    {
        c = c - 1;
        //pokud sloupec obsahuje delim
        if (strchr(table[c], delim) != NULL)
        {
            char helpT[100];
            int i = 0;
            int j = 0;
            //vytvori se pomocna tabulka, do ktere se nacte sloupec bez delimu
            while (i < (int)sizeof(helpT))
            {
                if (table[c][i] != delim)
                {
                    helpT[j] = table[c][i];
                    j++;
                }
                i++;
            }
            //tabulka se prevede na float
            float f = atof(helpT);
            //pokud neni cislo, nic se neudela
            if (f > 0)
            {
                //zaokrouhli se a preveda na int
                f = round(f);
                int F = (int)f;
                char charF[10];
                //zapise se do dalsi tabulky
                sprintf(charF, "%d", F);
                //ktera se nasledne napise do sloupce
                strcpy(table[c], charF);
                strncat(table[c - 1], &delim, 1);
            }
        }
        else
        { //prevedeni na float
            float f = atof(table[c]);
            if (f > 0)
            {
                //zaokrouhleni a zapsani do sloupce
                f = round(f);
                int F = (int)f;
                char charF[10];
                sprintf(charF, "%d", F);
                strcpy(table[c], charF);
            }
        }
        return EXIT_SUCCESS;
    }
    else
    {
        fprintf(stderr, "ERR: spatny argument");
        return EXIT_FAILURE;
    }
}

int toInt(char (*table)[size_col], char *C, char delim)
{

    int c = atoi(C);
    if (c > 0)
    {
        c = c - 1;
        //stejny postup jako u roundN, akorat nezakrouhluji, pouze prevadim na int
        if (strchr(table[c], delim) != NULL)
        {
            char helpT[100];
            int i = 0;
            int j = 0;
            while (i < (int)sizeof(helpT))
            {
                if (table[c][i] != delim)
                {
                    helpT[j] = table[c][i];
                    j++;
                }
                i++;
            }
            float f = atof(helpT);
            if (f > 0)
            {
                int F = (int)f;
                char charF[10];
                sprintf(charF, "%d", F);
                strcpy(table[c], charF);
                strncat(table[c - 1], &delim, 1);
            }
        }
        else
        {
            float f = atof(table[c]);
            if (f > 0)
            {
                int F = (int)f;
                char charF[10];
                sprintf(charF, "%d", F);
                strcpy(table[c], charF);
            }
        }
        return EXIT_SUCCESS;
    }
    else
    {
        fprintf(stderr, "ERR: spatny argument");
        return EXIT_FAILURE;
    }
}

int copy(char (*table)[size_col], int cols, char *N, char *M, char delim)
{
    int n = atoi(N);
    int m = atoi(M);
    //pokud je n mensi nez m a zaroven jsou obe cisla
    if (n < m && n > 0 && m > 0)
    {
        n = n - 1;
        m = m - 1;
        //kdyz se sloupec n nema delim
        if (strchr(table[n], delim) == NULL)
        {
            //zkopiruje se a prida se delim na konec predchoziho sloupce
            strcpy(table[m], table[n]);
            strncat(table[m - 1], &delim, 1);
        }
        else
        {
            strcpy(table[m], table[n]);
        }
        return EXIT_SUCCESS;
    }
    //pokud je m vetsi nez n, zaroven jsou obe cisla, m je mensi nez pocet sloupcu
    //nechci aby m bylo prepsano prazdnym neexistujicim sloupcem
    else if (m < n && n > 0 && m > 0 && m < cols + 1)
    {
        n = n - 1;
        m = m - 1;
        //pokud m neobsahuje delim
        if (strchr(table[m], delim) == NULL)
        {
            //zkopiruje se a odstrani se znak delimu
            strcpy(table[m], table[n]);
            char *col = strtok(table[m], &delim);
            strcpy(table[m], col);
        }
        else
        {
            strcpy(table[m], table[n]);
        }
        return EXIT_SUCCESS;
    }
    else
    {
        fprintf(stderr, "ERR: spatny argument");
        return EXIT_FAILURE;
    }
}

int swap(char (*table)[size_col], char *N, char *M, char delim)
{
    int n = atoi(N);
    int m = atoi(M);
    if (n > 0 && m > 0)
    {
        n = n - 1;
        m = m - 1;
        //pomocna tabulka
        char hTable[size_col];
        //osetreni, prvni sloupec neobsahuje delim
        //kdyz sloupec n nema delim a m ma delim
        if (strchr(table[n], delim) == NULL && strchr(table[m], delim) != NULL)
        {
            //sloupce se prohodi
            strcpy(hTable, table[n]);
            strcpy(table[n], table[m]);
            strcpy(table[m], hTable);
           
           
            //ze sloupce n se odstrani delim
            char *col = strtok(table[n], &delim);
            strcpy(table[n], col);
            //prida se delim na konec predchoziho sloupce m
            strncat(table[m - 1], &delim, 1);
        }
        //stejny postup, pouze opacne
        else if (strchr(table[n], delim) != NULL && strchr(table[m], delim) == NULL)
        {
            strcpy(hTable, table[n]);
            strcpy(table[n], table[m]);
            strcpy(table[m], hTable);
            
            char *col = strtok(table[m], &delim);
            strcpy(table[m], col);
            strncat(table[n - 1], &delim, 1);
        }
        else
        {
            strcpy(hTable, table[n]);
            strcpy(table[n], table[m]);
            strcpy(table[m], hTable);
        }

        return EXIT_SUCCESS;
    }
    else
    {
        fprintf(stderr, "ERR: spatny argument");
        return EXIT_FAILURE;
    }
}

int move(char (*table)[size_col], int cols, char *N, char *M, char delim)
{
    int n = atoi(N);
    int m = atoi(M);
    char hTable[size_col];
    if (n > 0 && m > 0)
    {
        n = n - 1;
        m = m - 1;
        //pokud je sloupec n mensi nez m a zaroven sloupec m mensi nez pocet sloupcu
        if (n < m && m <= cols)
        {
            //cyklus ktery presouva sloupec n az pred sloupec m
            //prohazuji se vzdy dva sloupce vedle sebe
            while (n < m - 1)
            {
                //kdyz sloupec neobsahuje delim
                if (strchr(table[n], delim) == NULL)
                {
                    //prohodi dva sloupce vedle sebe
                    strcpy(hTable, table[n]);
                    strcpy(table[n], table[n + 1]);
                    strcpy(table[n + 1], hTable);
                    memset(hTable, 0, (int)sizeof(hTable));
                    //odebere delim z prvniho sloupce
                    char *col = strtok(table[n], &delim);
                    strcpy(table[n], col);
                    //n+1-1=pridam delim na konec predchoziho sloupce
                    strncat(table[n], &delim, 1);
                }
                //pro vsechny zbyvajici sloupce
                else
                {
                    strcpy(hTable, table[n]);
                    strcpy(table[n], table[n + 1]);
                    strcpy(table[n + 1], hTable);
                }
                //vymazu obsah pomocny tabulky
                memset(hTable, 0, (int)sizeof(hTable));
                n++;
            }
        }
        //stejny postup, akorat n presunuju ze sloupce, ktery je vyssi
        else if (m < n && n <= cols)
        {
            //n musim tedy prohodit se sloupcem o jeden mensi
            //az bude pred sloupcem m
            while (m < n)
            {
                if (strchr(table[n - 1], delim) == NULL)
                {
                    strcpy(hTable, table[n]);
                    strcpy(table[n], table[n - 1]);
                    strcpy(table[n - 1], hTable);

                    char *col = strtok(table[n - 1], &delim);
                    strcpy(table[n - 1], col);
                    strncat(table[n - 1], &delim, 1);
                }
                else
                {
                    strcpy(hTable, table[n]);
                    strcpy(table[n], table[n - 1]);
                    strcpy(table[n - 1], hTable);
                }
                memset(hTable, 0, (int)sizeof(hTable));
                n--;
            }
        }
        return EXIT_SUCCESS;
    }
    else
    {
        fprintf(stderr, "ERR: spatny argument");
        return EXIT_FAILURE;
    }
}

int callProcessing(char (*table)[size_col], char **argv, char (*cmd_processing)[15], char delim, int columns, int i)
{
    //funkce pro volani funkcí zpracování
    //argument se porovnava s tabulkou zpracovani
    if (strcmp(argv[i], cmd_processing[0]) == 0)
    {
        int condition = cset(table, argv[i + 1], argv[i + 2], delim);
        //pokud se stala chyba, vrati se exit_failure
        if (condition == 1)
        {
            return EXIT_FAILURE;
        }
    }
    else if (strcmp(argv[i], cmd_processing[1]) == 0)
    {
        int condition = toLower(table, argv[i + 1]);
        if (condition == 1)
        {
            return EXIT_FAILURE;
        }
    }
    else if (strcmp(argv[i], cmd_processing[2]) == 0)
    {
        int condition = toUpper(table, argv[i + 1]);
        if (condition == 1)
        {
            return EXIT_FAILURE;
        }
    }
    else if (strcmp(argv[i], cmd_processing[3]) == 0)
    {
        int condition = roundN(table, argv[3], delim);
        if (condition == 1)
        {
            return EXIT_FAILURE;
        }
    }
    else if (strcmp(argv[i], cmd_processing[4]) == 0)
    {
        int condition = toInt(table, argv[i + 1], delim);
        if (condition == 1)
        {
            return EXIT_FAILURE;
        }
    }
    else if (strcmp(argv[i], cmd_processing[5]) == 0)
    {
        int condition = copy(table, columns, argv[i + 1], argv[i + 2], delim);
        if (condition == 1)
        {
            return EXIT_FAILURE;
        }
    }
    else if (strcmp(argv[i], cmd_processing[6]) == 0)
    {
        int condition = swap(table, argv[i + 1], argv[i + 2], delim);
        if (condition == 1)
        {
            return EXIT_FAILURE;
        }
    }
    else if (strcmp(argv[i], cmd_processing[7]) == 0)
    {
        int condition = move(table, columns, argv[i + 1], argv[i + 2], delim);
        if (condition == 1)
        {
            return EXIT_FAILURE;
        }
    }
    return EXIT_SUCCESS;
}

int action(int argc, char **argv, char (*cmd)[15])
{
    //funkce mi urci, ktery z argumentu se vola
    //pokud argument obsahuje danou funkci vrati se jedna, jinak 0
    int action = 0;
    for (int i = 0; i < argc; i++)
    {
        for (int j = 0; j < (int)sizeof(cmd); j++)
        {
            if (strcmp(argv[i], cmd[j]) == 0)
            {
                action = 1;
            }
        }
    }

    return action;
}

int main(int argc, char *argv[])
{
    //definovani bufferu nazvem str
    char str[size_row];
    char delim;

    //kontrola vstupu
    int check = control(argc, argv, &delim);
    //podminka jestli kontrola probehla spravne, jestli ne, ukonci se program
    if (check == 1)
    {
        return EXIT_FAILURE;
    }

    //definovani tabulky pro jednotlive sloupce
    char table[10][size_col];
    //radsi v tabulce nastavim vsechny nulove hodnoty, aby byla opravdu prazdna
    memset(table, 0, (int)sizeof(table));

    //promena pro radky
    int row = 1;
    //promena pro hodnotu sloupcu v prvnim radku(bude prepsana)
    int firstRow = 0;

    //seznam prikazu pro upravu
    char cmd_edit[][15] = {"irow", "arow", "drow", "drows", "icol", "acol", "dcol", "dcols"};
    char cmd_processing[][15] = {"cset", "tolower", "toupper", "round", "int", "copy", "swap", "move"};
    char cmd_selection[][15] = {"rows", "beginswith", "contains"};

    //volani funkce action, pro urceni, co argumenty obsahuji
    int cmd1 = action(argc, argv, cmd_edit);
    int cmd2 = action(argc, argv, cmd_processing);
    int cmd3 = action(argc, argv, cmd_selection);
    
    //radek se vlozim pres fgets do stringu
    while (fgets(str, size_row, stdin))
    {
        //vytvoreni nekolika promenych se kterymi budu nasledne pracovat
        int conditionDrow = 1;
        int i = 0;
        int col = 0;
        int del = 0;
        int j = 0;
        if (str == NULL)
        {
            fprintf(stderr, "ERR: soubor je prazdny");
            return EXIT_FAILURE;
        }
        else
        {

            //projizdi str zank po znaku do konce radku
            while (str[i] != '\000')
            {
                if (str[i] == delim)
                {
                    //kdyz se znak rovna delim, vytvori se novy sloupec pro tabulku
                    del++;
                    col++;
                    j = 0;
                }
                if (str[i] != '\n')
                {
                    //do sloupce se nepridava pouze znak konce radku
                    table[col][j] = str[i];
                }
                i++;
                j++;
            }

            //hodnota poctu sloupcu pro prvni radek
            if (row == 1)
            {
                firstRow = del;
            }

            //promeny, ktery jsem poslal nejdriv do funkce action
            //pokud argumenty obsahuji pouze funkce z tabulky pro upravu
            if (cmd1 == 1 && cmd2 == 0 && cmd3 == 0)
            {
                //projizdi argumenty, kdyz se rovna spusti se funkce
                for (int i = 1; i < argc; i++)
                {
                    if (strcmp(argv[i], cmd_edit[0]) == 0)
                    {
                        irow(firstRow, row, argv[i + 1], delim);
                    }
                    else if (strcmp(argv[i], cmd_edit[2]) == 0)
                    {
                        //drow function
                        int r = atoi(argv[i + 1]);
                        if (r > 0)
                        {
                            //nespustí vypsani tabulky pro daný řádek
                            if (row == r)
                            {
                                conditionDrow = 0;
                            }
                        }
                        else
                        {
                            fprintf(stderr, "ERR: spatny argument");
                            return EXIT_FAILURE;
                        }
                    }
                    else if (strcmp(argv[i], cmd_edit[3]) == 0)
                    {
                        //funkce drows
                        int n = atoi(argv[i+1]);
                        int m = atoi(argv[i+2]);
                        if (n > 0 && m > 0)
                        {
                            if (n <= m)
                            {
                                //nevypise radky n - m
                                if (n <= row && m >= row)
                                {
                                    conditionDrow=0;
                                }
                            }
                            else
                            {
                                fprintf(stderr, "ERR: prvni cislo musi byt mensi nez druhe \n");
                                return EXIT_FAILURE;
                            }
                        }
                        else
                        {
                            fprintf(stderr, "ERR: spatny argument \n");
                            return EXIT_FAILURE;
                        }

                    }
                    else if (strcmp(argv[i], cmd_edit[4]) == 0)
                    {
                        int condition = icol(table, argv[i + 1], delim);
                        if (condition == 1)
                        {
                            return EXIT_FAILURE;
                        }
                        //funkce pridala sloupec, pocet sloupcu se zvysil
                        del++;
                    }
                    else if (strcmp(argv[i], cmd_edit[5]) == 0)
                    {
                        acol(table, col, delim);
                        del++;
                    }
                    else if (strcmp(argv[i], cmd_edit[6]) == 0)
                    {
                        int condition = dcol(table, argv[i + 1]);
                        if (condition == 1)
                        {
                            return EXIT_FAILURE;
                        }
                        //funkce odebrala sloupec, pocet sloupcu se snizil
                        del--;
                    }
                    else if (strcmp(argv[i], cmd_edit[7]) == 0)
                    {
                        int condition = dcols(table, argv[i + 1], argv[i + 2]);
                        if (condition == 1)
                        {
                            return EXIT_FAILURE;
                        }
                        int n = atoi(argv[i + 1]);
                        int m = atoi(argv[i + 2]);
                        //spocitani poctu sloupcu
                        del = del - (m - n);
                    }
                }
            }

            //pokud se argumenty rovnaji funkcim selekce a zpracovani
            else if (cmd2 == 1 && cmd3 == 1)
            {
                int i = 0;
                //pokud uzivatel oddeli sloupce delimem, argumenty pro funkce zacinaji argv[3]
                if (strcmp(argv[1], "-d") == 0)
                {
                    i = 3;
                }
                //pokud ne, zacinaji argv[1]
                else
                {
                    i = 1;
                }

                //funkce rows
                if (strcmp(argv[i], cmd_selection[0]) == 0)
                {
                    //kdyz se n i m rovnaji -, selekce se provede na poslednim radku tabulky
                    if (argv[i + 1][0] == '-' && argv[i + 2][0] == '-')
                    {
                        if (feof(stdin))
                        {
                            int condition = callProcessing(table, argv, cmd_processing, delim, firstRow, i + 3);
                            if (condition == 1)
                            {
                                return EXIT_FAILURE;
                            }
                        }
                    }
                    //kdyz se m rovna pomlcka, zpracování je až do posledního řádku
                    else if (argv[i + 1][0] != '-' && argv[i + 2][0] == '-')
                    {
                        int n = atoi(argv[i + 1]);
                        if (n <= row)
                        {
                            int condition = callProcessing(table, argv, cmd_processing, delim, firstRow, i + 3);
                            if (condition == 1)
                            {
                                return EXIT_FAILURE;
                            }
                        }
                    }
                    else
                    {
                        int n = atoi(argv[i + 1]);
                        int m = atoi(argv[i + 2]);
                        if (n > 0 && m > 0)
                        {
                            if (n < m)
                            {
                                if (n <= row && m >= row)
                                {
                                    int condition = callProcessing(table, argv, cmd_processing, delim, firstRow, i + 3);
                                    if (condition == 1)
                                    {
                                        return EXIT_FAILURE;
                                    }
                                }
                            }
                            else
                            {
                                fprintf(stderr, "ERR: prvni cislo musi byt mensi nez druhe");
                                return EXIT_FAILURE;
                            }
                        }
                        else
                        {
                            fprintf(stderr, "ERR: spatny argument");
                            return EXIT_FAILURE;
                        }
                    }
                }
                //funkce beginswith
                else if (strcmp(argv[i], cmd_selection[1]) == 0)
                {
                    int c = atoi(argv[i + 1]);
                    if (c > 0)
                    {
                        c = c - 1;
                        char helpT[size_col];
                        int k = 0;
                        //pomoci nove tabulky helpT se nahraje sloupec bez delimu
                        for (int j = 0; j < (int)sizeof(table[c]); j++)
                        {
                            if (table[c][j] != delim)
                            {
                                helpT[k] = table[c][j];
                                k++;
                            }
                        }
                        //porovnani zda se sloupec rovna poli z argv
                        if (strcmp(argv[i + 2], helpT) == 0)
                        {

                            int condition = callProcessing(table, argv, cmd_processing, delim, firstRow, i + 3);
                            if (condition == 1)
                            {
                                return EXIT_FAILURE;
                            }
                        }
                    }
                }
                //funkce contains
                else if (strcmp(argv[i], cmd_selection[2]) == 0)
                {
                    int c = atoi(argv[i + 1]);
                    if (c > 0)
                    {
                        c = c - 1;
                        //pomoci funkce strstr najde shodnou cast retezce a spusti funkci
                        if (strstr(table[c], argv[i + 2]) != NULL)
                        {
                            int condition = callProcessing(table, argv, cmd_processing, delim, firstRow, i + 3);
                            if (condition == 1)
                            {
                                return EXIT_FAILURE;
                            }
                        }
                    }
                }
            }
            else if (cmd2 == 1 && cmd3 != 1)
            {
                int i = 0;
                if (strcmp(argv[1], "-d") == 0)
                {
                    i = 3;
                }
                else
                {
                    i = 1;
                }
                //zavola funkci, ktera najde a spusti funkci z argumentu
                int condition = callProcessing(table, argv, cmd_processing, delim, firstRow, i);
                if (condition == 1)
                {
                    return EXIT_FAILURE;
                }
            }

            if (row == 1)
            {
                firstRow = del;
            }
            //pokud se vola funkce drow, ci drows nevypise se radek

            if (conditionDrow == 1)
            {
                //vypsani tabulky
                print(table, firstRow);
            }

            //vyprazdneni cele tabulky, pro dalsi radek
            memset(table, 0, (int)sizeof(table));

            //pocitani radku
            row++;
        }
    }

    //pokud se jeden z argumentu rovna arow, spusti se funkce
    for (int i = 1; i < argc; i++)
    {
        if (strcmp(argv[i], cmd_edit[1]) == 0)
        {
            arow(firstRow, delim);
        }
    }

    return 0;
}