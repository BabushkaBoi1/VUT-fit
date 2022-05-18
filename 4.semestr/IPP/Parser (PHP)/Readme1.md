### Implementační dokumentace k 1. úloze do IPP 2021/2022
### Jméno a příjmení: Vojtěch Hájek
### Login: xhajek51
## Analyzátor kódu v IPPcode22 (parse.php)
***
Analyzátor kódu v IPPcode22 je implementován ve skriput parser.php. Skript načte 
zdrojový kód implementovaný v jazyce IPPcode22 a provede lexikální a syntaktickou 
analýzu. \
\
Skript zkontroluje správnost syntaxe. Pokud je kód v pořádku, skript vygeneruje xml
reprezentaci IPPcode22, jinak skončí chybou. Pro výpis nápovědu skriptu je možné 
zadat argument "--help".

### Popis analýzy instrukcí
***
Popis implementačních detailů skriptu parse.php:
1. Kontrola argumentů, případný výpis nápovědu, při argumentu "--help"
    - pokud je zadán jiný argument, skript skončí s chybou 10
2. Inicializace xml knihovny (XMLWriter)
3. Načtení zdrojového kódu IPPCode22
4. Kontrola prázdných řádků
   - pokud je řádek prázdný přeskočí se
5. Kontrola a zpracování komentářů
   - pokud je řádek pouze komentář přeskočí se
   - pokud je komentář část za kódem, ořízne se a zpracovává se pouze kód
6. Kontrola hlavičky souboru ".IPPCode22"
   - pokud chybí, skript se ukončí s chybou 21
7. Rozdělení řádku podle mezer
8. Analýza řádku
   - kontrola, zda je instrukce podporovatelná v jazyce IPPCode22, pokud byla načtena instrukce s nepodporovatelným operačním kódem, skript se ukončí s chybou 22
   - kontrola počtu operandů, pokud je operandu víc/míň skript se ukončí s chybou 23
   - kontrola lexikální a syntaktické správnosti, pokud je nesprávná skript se ukončí s chybou 23

Pro zjednodušení je implementována řadu funkcí, především pro porovnávání správnosti řetězce.
Dále pak funkci pro vygenerování xml instrukce.
### Výstup skriptu
***
Po správném zkontrolování lexikální a syntaktické analýzy se vygeneguruje XML. 
Pro její vygenerování se využívá knihovna XMLWriter. \
Přiklad:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<program language="IPPcode22">
    <instruction order="1" opcode="WRITE">
        <arg1 type="int">0</arg1>
    </instruction>
    <instruction order="2" opcode="WRITE">
        <arg1 type="string">ábč</arg1>
    </instruction>
</program>
