### Implementační dokumentace k 2. úloze do IPP 2021/2022
### Jméno a příjmení: Vojtěch Hájek
### Login: xhajek51
##  Interpret XML reprezentace kódu (interpret.py)	
***
Interpret XML reprezentace kódu je skript interpret.py.

### Popis skriptu
***
Skript se spustí a zavolá se funkce start(), která je implementována ve třídě Interpret. Funkce start() zavolá další funkce, které postupně zpracovávají interpret. 
První funkcí je argument(), který zpracuje vstupní parametry:
> --help (vypíše nápovědu skriptu) \
> --source=file (načte zdrojový xml soubor)\
> --input=file (načte soubor se vstupy)

Pokud chybí parametr source nebo input, jsou zdrojové soubory načítány ze standardního vstupu.

Dále skript zavolá funkci xml_tree(), která zkontroluje a zpracuje xml strukturu. Kontroluje se především parametry instrukce. 
Všechny instrukce se seřádí podle paramatru order a také se seřadí argumenty v instrukci. 
Celé pole instrukcí se nejdřív projde a pokud se najde instrukce typu Label, tak se inicializuje
jako objekt třídy LabelList a vloží se do pole Label_list.

Následně se zavolá funkce instr_execute(), která slouží pro zpracování a vykonání instrukcí. 
Zde se celé pole instrukcí projede cyklem a ke každé instrukci se zavolá funkce execute(), která vykoná danou instrukci.

Instrukce s funkcí jump (např. JUMP, JUMPIFEQ, ...) jsou implementovány, tak že se najde label v poli labelů a od čísla pořádí, uchovaného v label, se začnou vykonávat instrukce. 
Pokud se jedná o instrukci CALL uloží se pozice, ze které se skáče do pole Return_list. 
Takže v případě volání instrukce RETURN se skript vrací na instrukce od posledního čísla v poli.

Pokud se zavolá instrukce BREAK, vypíše se na standardní chybový výstup pozice v kódu, počet vykonanných instrukcí, globální rámec, lokální rámce a dočasný rámec. Například:

```console
pozice v kódu: 9
počet vykonaných instrukcí: 9
globální rámce: 
  a
  b
  c
lokální rámce: 
dočasný rámce: 
  Neinicializován 
```
### Implementace OOP
***
Skript je implementován pomocí objektově orientovaného programování (OOP). Třida Interpret je hlavní třida celého skriptu, která volá a řídí všechny funkce.
Po rozdělení xml struktury se každá instrukce inicializuje a uloží jako objekt její specifické třídy, pro výběr třídy slouží třída
Factory, která určí a vrátí objekt třídy pro specifickou instrukci (např. instrukce MOVE bude objekt třídy Move class).
Každá tato třída instrukce dědí z třídy Instruction. Třida Instruction v sobě uchovává pole argumentů, které jsou objekty třídy Argument. 

Dále je implementována třída Frame, která v sobě ukládá proměnné, které jsou do pole ukládány jako objekty třídy 
Variables. Další třídou je LabelList, která reprezentuje labely.  

### Třidy
***
- Interpret
- Factory 
- Label_list
- Frame
- Variable
- Instruction
  - Move, CreateFrame, PushFrame, PopFrame, Defvar, Call, Return, Pushs, Pops, Add, Sub, Mul, Idiv, Lt, Gt, Eq, And, Or, Not, Int2Char, Stri2Int, Read, Write, Concat, Strlen, Getchar, Setchar, Type, Label, Jump, JumpIfEq, JumpIfNEq, Exit, Dprint, Break,               
- Argument
  
