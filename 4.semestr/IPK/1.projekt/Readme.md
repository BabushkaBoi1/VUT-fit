# IPK projekt 1

#### author: Hájek Vojtěch (xhajek51)

První projekt pro předmět Počítačové komunikace a sítě. Úkolem bylo vytvořit jednoduchý http server
s třemi jednoduchými příkazy.

## Začínáme
Pro správné fungování programu je nutné ho nejdříve přeložit a to pomocí přiloženého Makefilu.

### Instalace:
Příkazy pro přeložení programu a pro smazání přeložení:

    make
    make clean

## Použití:
Příkazy:

    ./hinfosvc <port>
    GET /hostname
    GET /cpu-name
    GET /load

### Popis funkcí
hostname: vrací doménové jméno serveru \
cpu-name: vrací informace o procesoru \
load: vrací aktuální procento využítí procesoru

### Příklady užití
    $: ./hinfosvc 12345 & curl http://localhost:12345/cpu-name
    Intel(R) Core(TM) i5-10300H CPU @ 2.50GHz

    $: ./hinfosvc 12345 & GET http://localhost:12345/load
    14%
    
## Implementace

Nejprve program kontroluje zda uživatel zadal argument portu, na kterém následně bude běžet http server.
Dále se přejde na vytvoření socketu serveru, ten se nastaví, poté projde přes vrstvy bind a listen.
Následně čeká v nekonečném cyklu na klienta a jeho příkaz. Ten se zpracuje pomocí funkcí popsaných výše
a funkce send se vypíše na server. \
Ukončení serveru se provede pomoocí klávesové zkratky ctrl + c.



