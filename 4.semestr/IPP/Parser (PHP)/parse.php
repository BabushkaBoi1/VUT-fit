<?php
/**
 *  Skript pro analyzátor kódu v IPPcode22 (parse.php)
 *
 *  @author Hájek Vojtěch (xhajek51)
 */

ini_set('display_errors', 'stderr');

if ($argc > 1){
    if($argv[1] === "--help"){
        echo ("Usage: parser.php <STDIN\n");
        exit(0);
    } else{
        exit(10);
    }
}

// XMLWriter inicializace
$xw = new XMLWriter();
$xw->openMemory();
$xw->setIndent(true);
$xw->setIndentString('  ');
$xw->startDocument("1.0", "UTF-8");

$line_number = 1;
$header = false;

$file = fopen("test.IPPcode22", "r");
while ($line = fgets($file)){
    $arg = NULL;
    $type = NULL;
    // zpracování prázdného řádku
    $pom_line = trim($line, ' ');
    if(empty(trim($pom_line, "\n"))){
        continue;
    }
    // zpracování komentářů v kódu
    if(str_contains($line, '#')){
        $split_comments = explode('#', $line);
        if(empty(trim($split_comments[0], ' '))){
            continue;
        }
        $line = $split_comments[0];
    }
    $line = trim($line, ' ');
    // zpracování hlavičky
    if($line_number === 1 && !$header){
        if(preg_match("/^(\.IPPcode22)$/", $line)){
            $header = true;
            $xw->startElement("program");
            $xw->writeAttribute("language", "IPPcode22");
            continue;
        } else{
            exit(21);
        }
    }

    $line = preg_replace('/\s\s+/', ' ', $line);
    // rozdělení řádku podle mezer
    $split_line = explode(' ', trim($line, "\n"));
    $count=0;
    while (!empty($split_line[$count])) $count++;
    $n_arg = 0;

    // kontrola syntaxe kódu
    switch (strtoupper($split_line[0])){
        case 'MOVE':
        case 'INT2CHAR':
        case 'STRLEN':
        case 'TYPE':
        case 'NOT':
            if($count != 3) exit(23);
            if(preg_variable($split_line[1])){
                $arg[$n_arg]=$split_line[1];
                $type[$n_arg] = "var";
                if($type[1] = preg_symbol($split_line[2])){
                    $n_arg++;
                    $arg[$n_arg]=$split_line[2];
                }
            }
            break;
        case 'CREATEFRAME':
        case 'PUSHFRAME':
        case 'POPFRAME':
        case 'RETURN':
        case 'BREAK':
            if($count != 1) exit(23);
            break;
        case 'DEFVAR':
        case 'POPS':
            if($count != 2) exit(23);
            if(preg_variable($split_line[1])){
                $arg[$n_arg] = $split_line[1];
                $type[$n_arg] = "var";
            }
            break;
        case 'CALL':
        case 'LABEL':
        case 'JUMP':
            if($count != 2) exit(23);
            if(preg_label($split_line[1])){
                $arg[$n_arg] = $split_line[1];
                $type[$n_arg] = "label";
            }
            break;
        case 'PUSHS':
        case 'WRITE':
        case 'EXIT':
        case 'DPRINT':
            if($count != 2) exit(23);
            if($type[0] = preg_symbol($split_line[1])){
                $arg[$n_arg] = $split_line[1];
            }
            break;
        case 'ADD':
        case 'SUB':
        case 'MUL':
        case 'IDIV':
        case 'LT':
        case 'GT':
        case 'EQ':
        case 'AND':
        case 'OR':
        case 'STRI2INT':
        case 'CONCAT':
        case 'GETCHAR':
        case 'SETCHAR':
            if($count != 4) exit(23);
            if(preg_variable($split_line[1])){
                $arg[$n_arg] = $split_line[1];
                $type[$n_arg] = "var";
                if ($type[1] = preg_symbol($split_line[2])){
                    $n_arg++;
                    $arg[$n_arg] = $split_line[2];
                    if($type[2] = preg_symbol($split_line[3])){
                        $n_arg++;
                        $arg[$n_arg] = $split_line[3];
                    }
                }
            }
            break;
        case 'READ':
            if($count != 3) exit(23);
            if(preg_variable($split_line[1])){
                $arg[$n_arg] = $split_line[1];
                $type[$n_arg] = "var";
                if(preg_type($split_line[2])){
                    $n_arg++;
                    $arg[$n_arg] = $split_line[2];
                    $type[$n_arg] = "type";
                }
            }
            break;
        case 'JUMPIFEQ':
        case 'JUMPIFNEQ':
            if($count != 4) exit(23);
            if(preg_label($split_line[1])){
                $arg[$n_arg] = $split_line[1];
                $type[$n_arg] = "label";
                if($type[1] = preg_symbol($split_line[2])){
                    $n_arg++;
                    $arg[$n_arg] = $split_line[2];
                    if($type[2] = preg_symbol($split_line[3])){
                        $n_arg++;
                        $arg[$n_arg] = $split_line[3];
                    }
                }
            }
            break;
        default:
            exit(22);
    }

    xml_instruction($xw, $split_line[0], $type, $arg, $line_number);
    $line_number++;
}
$xw->endElement();
$xw->endDocument();
echo $xw->outputMemory();

/**
 *  Funkce pro výpis xml instrukce
 *
 *  @param xml $xml
 *  @param string $opcode
 *  @param string $type
 *  @param string $arg
 *  @param int $order
 */
function xml_instruction($xml, $opcode, $type, $arg, $order){
    $xml->startElement("instruction");
        $xml->writeAttribute("order", $order);
        $xml->writeAttribute("opcode", strtoupper($opcode));
        if(!empty($arg)){
            $i = 0;
            foreach ($arg as $argument){
                $xml->startElement("arg".($i+1));
                $xml->writeAttribute("type", $type[$i]);
                if(str_contains($argument, '@') && $type[$i]!="var"){
                    $argument1 = explode('@', $argument, 2);
                    $argument = $argument1[1];
                }
                $xml->text($argument);
                $xml->endElement();
                $i++;
            }
        }
    $xml->endElement();

}

/**
 *  Funkce pro určení správnosti řetězce typu variable
 *
 *  @param string $func_line
 */
function preg_variable($func_line){
    if(preg_match("/^(LF|GF|TF)@(([a-zA-Z\$\_\-\&\%\*\!\?])+(\d)*)+$/", $func_line)){
        return true;
    }
    exit(23);
}

/**
 *  Funkce pro určení správnosti řetězce typu symbol
 *
 *  @param string $func_line
 *  @return string typ
 */
function preg_symbol($func_line){
    if(preg_match("/^int@((-|\+)?[1-9]+[0-9]*|0)+$/", $func_line)){
        return "int";
    }
    if(preg_match("/^string@((\\\\\d{3})|[a-zA-Zá-žÁ-Ž0-9$<>\@\/\_\,\.\;\§\=\(\)\-\!\?\%\*\&])*$/", $func_line)){
        return "string";
    }
    if(preg_match("/^bool@(true|false)$/", $func_line)){
        return "bool";
    }
    if(preg_match("/^nil@nil$/", $func_line)){
        return "nil";
    }
    if(preg_match("/^(LF|GF|TF)@[a-zA-Z$\_\-\&\%\*\!\?]*$/", $func_line)){
        return "var";
    }
    exit(23);
}

/**
 *  Funkce pro určení správnosti řetězce typu type
 *
 *  @param string $func_line
 */
function preg_type($func_line){
    if(preg_match("/^(int|bool|string)$/", $func_line)){
        return true;
    }
    exit(23);
}


/**
 *  Funkce pro určení správnosti řetězce typu label
 *
 *  @param string $func_line
 */
function preg_label($func_line){
    if(preg_match("/^[a-zA-Z$\-\_\&\%\*\!\?]*$/", $func_line)){
        return true;
    }
    exit(23);
}

?>