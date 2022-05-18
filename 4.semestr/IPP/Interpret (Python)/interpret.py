# IPPcode22 interpret
# @author Vojtěch Hájek(xhajek51)


import re
import sys
from sys import stderr
import argparse
import xml.etree.ElementTree as ET


def check_int(value):
    """
    check_int function is for checking type int
    :param value: variable value
    :return: true / false
    """
    try:
        int(value)
        return True
    except ValueError:
        return False
    except TypeError:
        return False


def check_bool(value):
    """
    check_bool function is for checking type bool
    :param value: variable value
    :return: true / false
    """
    try:
        if value == "true" or value == "false":
            return True
        return False
    except ValueError:
        return False


def check_nil(value):
    """
    check_nil function is for checking type nil
    :param value: variable value
    :return: true / false
    """
    try:
        if value == "nil":
            return True
        return False
    except ValueError:
        return ValueError


def check_str(value):
    """
    check_str function is for checking type int
    :param value: variable value
    :return: true / false
    """
    try:
        str(value)
        return True
    except ValueError:
        return False


def get_bool(value):
    """
    check_bool function is for checking type int
    :param value: variable value
    :return: true / false
    """
    if value == "true":
        return True
    else:
        return False


class Instruction:
    """
    the Instruction class
    :param opcode: type of instruction
    :param args: field of arguments
    :param order: order number
    """
    def __init__(self, opcode, args, order):
        self.opcode = opcode
        self.args = args
        self.order: int = order

    def get_name(self):
        return self.opcode

    def get_args(self):
        return self.args


class Argument:
    """
    the Argument class
    :param arg_type: type of argument
    :param value: variable of value
    :param number: order number
    """
    def __init__(self, arg_type, value, number):
        self.arg_type = arg_type
        self.value = value
        self.number = number


class LabelList:
    """
    the LabelList class
    :param name: name of label
    :param number: order number
    """
    def __init__(self, name, number):
        self.name = name
        self.number = number


class Frame:
    """
    the Frame class to store frames
    :ivar variables: field of variables
    """
    def __init__(self):
        self.variables = []

    def add_value(self, name, value, type_var):
        self.variables.append(Variable(name, value, type_var))


class Variable:
    """
    the Frame class to define variable
    :param name: variable name
    :param value: value of variable
    :param type_var: type of variable
    """
    def __init__(self, name, value, type_var):
        self.name = name
        self.value = value
        self.type = type_var

    def get_type(self):
        return self.type

    def get_value(self):
        return self.value

    def set_type(self, type_var):
        self.type = type_var

    def set_value(self, value):
        self.value = value


class Interpret:
    """
    the Interpret class to run interpret
    :ivar global_frame: frame
    :ivar local_frame: field of local frames
    :ivar temporary_frame: not initialized frame
    :ivar instruction_list: field of instructions
    :ivar label_list: field of labels
    :ivar return_list: field of return instruction numbers
    :ivar stack_list: field for stack
    :ivar file: variable of file name (source)
    :ivar input: variable of file name (input)
    :ivar input_number: number of line in input file
    """
    def __init__(self):
        self.global_frame = Frame()
        self.local_frame = []
        self.temporary_frame = None
        self.instruction_list = []
        self.label_list = []
        self.return_list = []
        self.stack_list = []
        self.file = None
        self.input = None
        self.input_number = 0

    def start(self):
        """
        function start to start interpret and call fucntions
        """
        self.argument()
        self.xml_etree()
        self.instr_execute()

    def argument(self):
        """
        argument function to process parametrs
        """
        argp = argparse.ArgumentParser()
        argp.add_argument("--source", nargs=1, help="")
        argp.add_argument("--input", nargs=1, help="")
        args = vars(argp.parse_args())
        self.file = args.get("source")
        self.input = args.get("input")

    def xml_etree(self):
        """
        xml_etree function is for parse xml structure and check syntax of xml
        """
        try:
            if self.file is not None:
                tree = ET.parse(self.file[0])
            else:
                tree = ET.parse(sys.stdin)
        except IndexError:
            exit(31)
        except ET.ParseError:
            exit(31)
        root = tree.getroot()
        if root.tag != 'program':
            exit(32)
        for child in root:
            if child.tag != 'instruction':
                exit(32)
            attr_list = list(child.attrib.keys())
            if not ('order' in attr_list) or not ('opcode' in attr_list):
                exit(32)
            for subelem in child:
                if not (re.match(r"arg[123]", subelem.tag)):
                    exit(32)
                attrs_list = list(subelem.attrib.keys())
                if not ('type' in attrs_list):
                    exit(32)

        for iroot in root:
            args = []
            for ichild in iroot:
                arg_num = ichild.tag.strip("arg")
                args.append(Argument(ichild.attrib.get("type"), ichild.text, arg_num))
            self.args_sort(args)
            try:
                i1 = Factory.make(iroot.attrib.get("opcode"), args, int(iroot.attrib.get("order")))
            except ValueError:
                exit(32)
            self.instruction_list.append(i1)
        self.sorted()
        self.add_labels()

    def sorted(self):
        """
        sorted function is for sort instruction by order number
        """
        self.instruction_list.sort(key=lambda x: x.order)

    def args_sort(self, args):
        """
        args_sort function is for sort arguments in instruction
        """
        args.sort(key=lambda x: x.number)
        i = 0
        j = 1
        while i < len(args):
            if j != int(args[i].number):
                exit(32)
            i += 1
            j += 1

    def add_labels(self):
        """
        add_labels function is for find labels in instruction list
        """
        i = 0
        while i < len(self.instruction_list):
            inst = self.instruction_list[i]
            if inst.order < 1:
                exit(32)
            if inst.order == self.instruction_list[i-1].order and i != 0:
                exit(32)
            if inst.opcode == "LABEL":
                self.add_label(inst.args[0].value, i)
            i += 1

    def add_label(self, name, number):
        """
        add_label function is for add label to list
        """
        for lab in self.label_list:
            if lab.name == name:
                exit(52)
        self.label_list.append(LabelList(name, number))

    def instr_execute(self):
        """
        instr_execute function is for process instructions
        """
        i = 0
        j = 0
        while i < len(self.instruction_list):
            instr: Instruction = self.instruction_list[i]
            if instr.opcode == "JUMP" or instr.opcode == "CALL" or instr.opcode == "RETURN" or instr.opcode == "JUMPIFEQ" or instr.opcode == "JUMPIFNEQ":
                i = instr.execute(self, i)
            elif instr.opcode == "BREAK":
                instr.execute(self, j)
                i = i + 1
            else:
                instr.execute(self)
                i = i + 1
            j = j + 1

    def get_gf(self):
        """
        get_gf function
        :return global_frame
        """
        return self.global_frame

    def get_lf(self):
        """
        get_lf function
        :return local_frame
        """
        return self.local_frame

    def get_tf(self):
        """
        get_tf function
        :return temporary_frame
        """
        return self.temporary_frame

    def find(self, name):
        """
        find function is for finding variable by name
        :return variable from frame
        """
        try:
            if re.match("^GF", name):
                for var in self.global_frame.variables:
                    name = name.strip("GF@")
                    if var.name == name:
                        return var
                exit(54)
            elif re.match("^LF", name):
                name = name.strip("LF@")
                for var in self.local_frame[len(self.local_frame) - 1].variables:
                    if var.name == name:
                        return var
                exit(54)
            elif re.match("^TF", name):
                name = name.strip("TF@")
                if self.temporary_frame is None:
                    exit(55)
                for var in self.temporary_frame.variables:
                    if var.name == name:
                        return var
                exit(54)
            else:
                exit(58)
        except IndexError:
            exit(55)

    def find_label(self, name):
        """
        find_label function is for finding label by name
        :return label number
        """
        try:
            for label in self.label_list:
                if label.name == name:
                    return label.number
            exit(52)
        except IndexError:
            exit(52)

    def push_tf(self):
        """
        push_tf function is for transfer temporary frame to local frame and removes himself
        """
        if self.temporary_frame is not None:
            self.local_frame.append(self.temporary_frame)
            self.temporary_frame = None

    def add_tf(self):
        """
        add_tf function is for initialized temporary frame
        """
        self.temporary_frame = Frame()

    def get_value(self, arg):
        """
        get_value function is for get value from variable or argument
        :return value of variable or argument
        """
        if arg is None:
            return None
        elif re.match("^GF", arg) or re.match("^LF", arg) or re.match("^TF", arg):
            var1: Variable = self.find(arg)
            if var1.type is None:
                return None
            return var1.get_value()
        else:
            return arg

    def get_type(self, arg):
        """
        get_type function is for get type from variable or argument
        :return type of variable or argument
        """
        if arg.arg_type == "var":
            var = self.find(arg.value)
            if var.get_type() is None:
                exit(56)
            return var.get_type()
        else:
            return arg.arg_type

    def check_same_type(self, value1, value2):
        """
        check_same_type function
        :return true if same type / false
        """
        if check_int(value1) and check_int(value2):
            return True
        elif check_bool(value1) and check_bool(value2):
            return True
        elif check_nil(value1) and check_nil(value2):
            return True
        elif check_str(value1) and check_str(value2):
            return True
        else:
            return False


class Move(Instruction):
    def __init__(self, args, order):
        super().__init__("MOVE", args, order)

    def execute(self, inter: Interpret):
        found0 = inter.find(self.args[0].value)
        if self.args[1].arg_type == "var":
            found1 = inter.find(self.args[1].value)
            if found1.get_value() is None:
                exit(56)
            found0.set_value(found1.get_value())
            found0.set_type(found1.get_type())
        else:
            found0.set_value(self.args[1].value)
            found0.set_type(self.args[1].arg_type)


class CreateFrame(Instruction):
    def __init__(self, args, order):
        super().__init__("CREATEFRAME", args, order)

    def execute(self, inter: Interpret):
        inter.temporary_frame = Frame()


class PushFrame(Instruction):
    def __init__(self, args, order):
        super().__init__("PUSHFRAME", args, order)

    def execute(self, inter: Interpret):
        if bool(inter.temporary_frame):
            inter.local_frame.append(inter.temporary_frame)
            inter.temporary_frame = None
        else:
            exit(55)


class PopFrame(Instruction):
    def __init__(self, args, order):
        super().__init__("POPFRAME", args, order)

    def execute(self, inter: Interpret):
        if bool(inter.local_frame):
            inter.temporary_frame = inter.local_frame.pop()
        else:
            exit(55)


class Defvar(Instruction):
    def __init__(self, args, order):
        super().__init__("DEFVAR", args, order)

    def execute(self, inter: Interpret):
        if re.match("GF", self.args[0].value):
            value = self.args[0].value.strip("GF@")
            for var in inter.global_frame.variables:
                if var.name == value:
                    exit(52)
            inter.global_frame.add_value(value, None, None)
        elif re.match("LF", self.args[0].value):
            value = self.args[0].value.strip("LF@")
            if bool(inter.local_frame):
                for var in inter.local_frame[len(inter.local_frame)-1].variables:
                    if var.name == value:
                        exit(52)
                inter.local_frame[len(inter.local_frame)-1].add_value(value, None, None)
            else:
                exit(55)
        elif re.match("TF", self.args[0].value):
            if inter.temporary_frame is None:
                exit(55)
            value = self.args[0].value.strip("TF@")
            for var in inter.temporary_frame.variables:
                if var.name == value:
                    exit(52)
            inter.temporary_frame.add_value(value, None, None)
        else:
            exit(32)


class Call(Instruction):
    def __init__(self, args, order):
        super().__init__("CALL", args, order)

    def execute(self, inter: Interpret, line):
        inter.return_list.append(line)
        return inter.find_label(self.args[0].value)


class Return(Instruction):
    def __init__(self, args, order):
        super().__init__("RETURN", args, order)

    def execute(self, inter: Interpret, line):
        try:
            i = inter.return_list[len(inter.return_list)-1]
            inter.return_list.pop()
            return i + 1
        except IndexError:
            exit(56)


class Pushs(Instruction):
    def __init__(self, args, order):
        super().__init__("PUSHS", args, order)

    def execute(self, inter: Interpret):
        if re.match("^GF", self.args[0].value) or re.match("^LF", self.args[0].value) or re.match("^TF", self.args[0].value):
            var = inter.find(self.args[0].value)
            if var.type is None:
                exit(56)
            inter.stack_list.append(var)
        else:
            var = Variable(None, self.args[0].value, self.args[0].arg_type)
            inter.stack_list.append(var)


class Pops(Instruction):
    def __init__(self, args, order):
        super().__init__("POPS", args, order)

    def execute(self, inter: Interpret):
        if bool(inter.stack_list):
            var = inter.find(self.args[0].value)
            temp = inter.stack_list.pop()
            var.set_value(temp.value)
            var.set_type(temp.type)
        else:
            exit(56)


class Add(Instruction):
    def __init__(self, args, order):
        super().__init__("ADD", args, order)

    def execute(self, inter: Interpret):
        type_value1 = inter.get_type(self.args[1])
        type_value2 = inter.get_type(self.args[2])
        if type_value1 == "int" and type_value2 == "int":
            var = inter.find(self.args[0].value)
            value1 = inter.get_value(self.args[1].value)
            value2 = inter.get_value(self.args[2].value)
            try:
                value_r = int(value1) + int(value2)
            except ValueError:
                exit(32)
            var.set_value(str(value_r))
            var.set_type("int")
        else:
            exit(53)


class Sub(Instruction):
    def __init__(self, args, order):
        super().__init__("SUB", args, order)

    def execute(self, inter: Interpret):
        type_value1 = inter.get_type(self.args[1])
        type_value2 = inter.get_type(self.args[2])
        if type_value1 == "int" and type_value2 == "int":
            var = inter.find(self.args[0].value)
            value1 = inter.get_value(self.args[1].value)
            value2 = inter.get_value(self.args[2].value)
            try:
                value_r = int(value1) - int(value2)
            except ValueError:
                exit(32)
            var.set_value(str(value_r))
            var.set_type("int")
        else:
            exit(53)


class Mul(Instruction):
    def __init__(self, args, order):
        super().__init__("MUL", args, order)

    def execute(self, inter: Interpret):
        type_value1 = inter.get_type(self.args[1])
        type_value2 = inter.get_type(self.args[2])
        if type_value1 == "int" and type_value2 == "int":
            var = inter.find(self.args[0].value)
            value1 = inter.get_value(self.args[1].value)
            value2 = inter.get_value(self.args[2].value)
            try:
                value_r = int(value1) * int(value2)
            except ValueError:
                exit(32)
            var.set_value(str(value_r))
            var.set_type("int")
        else:
            exit(53)


class Idiv(Instruction):
    def __init__(self, args, order):
        super().__init__("IDIV", args, order)

    def execute(self, inter: Interpret):
        type_value1 = inter.get_type(self.args[1])
        type_value2 = inter.get_type(self.args[2])
        if type_value1 == "int" and type_value2 == "int":
            var = inter.find(self.args[0].value)
            value1 = inter.get_value(self.args[1].value)
            value2 = inter.get_value(self.args[2].value)
            try:
                value_r = int(value1) // int(value2)
                var.set_value(str(value_r))
                var.set_type("int")
            except ValueError:
                exit(32)
            except ZeroDivisionError:
                exit(57)
        else:
            exit(53)


class Lt(Instruction):
    def __init__(self, args, order):
        super().__init__("LT", args, order)

    def execute(self, inter: Interpret):
        type_value1 = inter.get_type(self.args[1])
        type_value2 = inter.get_type(self.args[2])
        if type_value1 == type_value2:
            var = inter.find(self.args[0].value)
            value1 = inter.get_value(self.args[1].value)
            value2 = inter.get_value(self.args[2].value)
            if value1 is None:
                value1 = ""
            if value2 is None:
                value2 = ""
            if type_value1 == "string":
                value1 = re.sub(r'\\([0-9]{3})', lambda x: chr(int(x[1])), str(value1))
                value2 = re.sub(r'\\([0-9]{3})', lambda x: chr(int(x[1])), str(value2))
            if value1 == "false" and value2 == "true":
                value_r = "true"
            elif value1 == "nil":
                exit(53)
            elif type_value1 == "int" and int(value1) < int(value2):
                value_r = "true"
            elif type_value1 != "int" and value1 < value2:
                value_r = "true"
            else:
                value_r = "false"
            var.set_value(value_r)
            var.set_type("bool")
        else:
            exit(53)


class Gt(Instruction):
    def __init__(self, args, order):
        super().__init__("GT", args, order)

    def execute(self, inter: Interpret):
        type_value1 = inter.get_type(self.args[1])
        type_value2 = inter.get_type(self.args[2])
        if type_value1 == type_value2:
            var = inter.find(self.args[0].value)
            value1 = inter.get_value(self.args[1].value)
            value2 = inter.get_value(self.args[2].value)
            if inter.check_same_type(value1, value2):
                if value1 is None:
                    value1 = ""
                if value2 is None:
                    value2 = ""
                if type_value1 == "string":
                    value1 = re.sub(r'\\([0-9]{3})', lambda x: chr(int(x[1])), str(value1))
                    value2 = re.sub(r'\\([0-9]{3})', lambda x: chr(int(x[1])), str(value2))
                if value1 == "true" and value2 == "false":
                    value_r = "true"
                elif value1 == "nil":
                    exit(53)
                elif type_value1 == "int" and int(value1) > int(value2):
                    value_r = "true"
                elif type_value1 != "int" and value1 > value2:
                    value_r = "true"
                else:
                    value_r = "false"
            var.set_value(value_r)
            var.set_type("bool")
        else:
            exit(53)


class Eq(Instruction):
    def __init__(self, args, order):
        super().__init__("EQ", args, order)

    def execute(self, inter: Interpret):
        type_value1 = inter.get_type(self.args[1])
        type_value2 = inter.get_type(self.args[2])
        if type_value1 == type_value2 or type_value1 == "nil" or type_value2 == "nil":
            var = inter.find(self.args[0].value)
            value1 = inter.get_value(self.args[1].value)
            value2 = inter.get_value(self.args[2].value)
            if value1 is None:
                value1 = ""
            if value2 is None:
                value2 = ""
            if type_value1 == "string":
                value1 = re.sub(r'\\([0-9]{3})', lambda x: chr(int(x[1])), str(value1))
                value2 = re.sub(r'\\([0-9]{3})', lambda x: chr(int(x[1])), str(value2))
            if value1 == value2:
                value_r = "true"
            else:
                value_r = "false"
            var.set_value(value_r)
            var.set_type("bool")
        else:
            exit(53)


class And(Instruction):
    def __init__(self, args, order):
        super().__init__("AND", args, order)

    def execute(self, inter: Interpret):
        type_value1 = inter.get_type(self.args[1])
        type_value2 = inter.get_type(self.args[2])
        if type_value1 == type_value2 and type_value1 == "bool":
            var = inter.find(self.args[0].value)
            value1 = inter.get_value(self.args[1].value)
            value2 = inter.get_value(self.args[2].value)
            value1 = get_bool(value1)
            value2 = get_bool(value2)
            if value1 and value2:
                value_r = "true"
            else:
                value_r = "false"
            var.set_value(value_r)
            var.set_type("bool")
        else:
            exit(53)


class Or(Instruction):
    def __init__(self, args, order):
        super().__init__("OR", args, order)

    def execute(self, inter: Interpret):
        type_value1 = inter.get_type(self.args[1])
        type_value2 = inter.get_type(self.args[2])
        if type_value1 == type_value2 and type_value1 == "bool":
            var = inter.find(self.args[0].value)
            value1 = inter.get_value(self.args[1].value)
            value2 = inter.get_value(self.args[2].value)
            value1 = get_bool(value1)
            value2 = get_bool(value2)
            if value1 or value2:
                value_r = "true"
            else:
                value_r = "false"
            var.set_value(value_r)
            var.set_type("bool")
        else:
            exit(53)


class Not(Instruction):
    def __init__(self, args, order):
        super().__init__("NOT", args, order)

    def execute(self, inter: Interpret):
        type_value1 = inter.get_type(self.args[1])
        if type_value1 == "bool":
            var = inter.find(self.args[0].value)
            value1 = inter.get_value(self.args[1].value)
            value1 = get_bool(value1)
            if not value1:
                value_r = "true"
            else:
                value_r = "false"
            var.set_value(value_r)
            var.set_type("bool")
        else:
            exit(53)


class Int2Char(Instruction):
    def __init__(self, args, order):
        super().__init__("INT2CHAR", args, order)

    def execute(self, inter: Interpret):
        var = inter.find(self.args[0].value)
        value_type = inter.get_type(self.args[1])
        if value_type == "int":
            value = inter.get_value(self.args[1].value)
            try:
                var.set_value(chr(int(value)))
                var.set_type("string")
            except ValueError:
                exit(58)
        else:
            exit(53)


class Stri2Int(Instruction):
    def __init__(self, args, order):
        super().__init__("STRI2INT", args, order)

    def execute(self, inter: Interpret):
        value1_type = inter.get_type(self.args[1])
        value2_type = inter.get_type(self.args[2])
        if value1_type == "string" and value2_type == "int":
            var = inter.find(self.args[0].value)
            value1 = inter.get_value(self.args[1].value)
            value2 = inter.get_value(self.args[2].value)
            if check_int(value2) is False:
                exit(53)
            if 0 <= int(value2) < len(value1):
                var.set_value(ord(value1[int(value2)]))
                var.set_type("int")
            else:
                exit(58)
        else:
            exit(53)


class Read(Instruction):
    def __init__(self, args, order):
        super().__init__("READ", args, order)
        self.input_list = []

    def execute(self, inter: Interpret):
        var =  inter.find(self.args[0].value)
        type = self.args[1].value
        try:
            if inter.input is not None:
                with open(inter.input[0], 'r') as file:
                    self.input_list = [line.strip() for line in file]
                inp = self.input_list[inter.input_number]
                inter.input_number+=1
            else:
                inp = input()
        except ValueError:
            var.set_value("nil")
            var.set_type("nil")
            return
        except EOFError:
            var.set_value("nil")
            var.set_type("nil")
            return
        if type == "bool":
            bool = get_bool(inp.lower())
            if bool:
                inp = "true"
            else:
                inp = "false"
        elif type == "int":
            if check_int(inp) is False:
                var.set_value("nil")
                var.set_type("nil")
                return
        elif type == "string":
            if check_str(inp) is False:
                var.set_value("nil")
                var.set_type("nil")
                return
        elif type == "nil":
            if inp != "nil":
                var.set_value("nil")
                var.set_type("nil")
                return
        var.set_value(inp)
        var.set_type(type)


class Write(Instruction):
    def __init__(self, args, order):
        super().__init__("WRITE", args, order)

    def execute(self, inter: Interpret):
        value_type = inter.get_type(self.args[0])
        value = inter.get_value(self.args[0].value)
        if value is None:
            return
        if value_type != "nil":
            try:
                value = str(value)
                value = re.sub(r'\\([0-9]{3})', lambda x: chr(int(x[1])), value)
                print(value, end='')
            except TypeError:
                print("", end='')
        else:
            print("", end='')


class Concat(Instruction):
    def __init__(self, args, order):
        super().__init__("CONCAT", args, order)

    def execute(self, inter: Interpret):
        type_value1 = inter.get_type(self.args[1])
        type_value2 = inter.get_type(self.args[2])
        if type_value1 == type_value2 and type_value1 == "string":
            var = inter.find(self.args[0].value)
            value1 = inter.get_value(self.args[1].value)
            value2 = inter.get_value(self.args[2].value)
            if check_str(value1) and check_str(value2):
                try:
                    var.set_value(value1 + value2)
                    var.set_type("string")
                except TypeError:
                    var.set_value(None)
                    var.set_type("string")
            else:
                exit(58)
        else:
            exit(53)


class Strlen(Instruction):
    def __init__(self, args, order):
        super().__init__("STRLEN", args, order)

    def execute(self, inter: Interpret):
        type_value = inter.get_type(self.args[1])
        if type_value == "string":
            var = inter.find(self.args[0].value)
            value = inter.get_value(self.args[1].value)
            if value is None:
                var.set_value(0)
                var.set_type("int")
                return
            value = re.sub(r'\\([0-9]{3})', lambda x: chr(int(x[1])), value)
            if check_str(value):
                var.set_value(len(str(value)))
                var.set_type("int")
            else:
                exit(58)
        else:
            exit(53)


class Getchar(Instruction):
    def __init__(self, args, order):
        super().__init__("GETCHAR", args, order)

    def execute(self, inter: Interpret):
        type_value1 = inter.get_type(self.args[1])
        type_value2 = inter.get_type(self.args[2])
        if type_value1 == "string" and type_value2 == "int":
            var = inter.find(self.args[0].value)
            value1 = inter.get_value(self.args[1].value)
            value2 = inter.get_value(self.args[2].value)
            value1 = re.sub(r'\\([0-9]{3})', lambda x: chr(int(x[1])), value1)
            if check_str(value1) and check_int(value2):
                if 0 <= int(value2) < len(str(value1)):
                    var.set_value(value1[int(value2)])
                    var.set_type("string")
                else:
                    exit(58)
            else:
                exit(53)
        else:
            exit(53)


class Setchar(Instruction):
    def __init__(self, args, order):
        super().__init__("SETCHAR", args, order)

    def execute(self, inter: Interpret):
        type_value1 = inter.get_type(self.args[1])
        type_value2 = inter.get_type(self.args[2])
        var = inter.find(self.args[0].value)
        if var.get_type() is None:
            exit(56)
        if type_value1 == "int" and type_value2 == "string" and var.get_type() == "string":
            value1 = inter.get_value(self.args[1].value)
            value2 = inter.get_value(self.args[2].value)
            if value2 is None:
                exit(58)
            value2 = re.sub(r'\\([0-9]{3})', lambda x: chr(int(x[1])), value2)
            if check_int(value1) and check_str(value2):
                temp = var.get_value()
                if temp is None:
                    exit(56)
                str_temp = list(temp)
                if 0 <= int(value1) < len(temp) and bool(value2):
                    value2 = list(value2)
                    str_temp[int(value1)] = value2[0]
                    temp = "".join(str_temp)
                    var.set_value(temp)
                else:
                    exit(58)
        else:
            exit(53)


class Type(Instruction):
    def __init__(self, args, order):
        super().__init__("TYPE", args, order)

    def execute(self, inter: Interpret):
        var = inter.find(self.args[0].value)
        if self.args[1].arg_type == "var":
            temp = inter.find(self.args[1].value)
            if temp.type is None:
                var.set_value("")
                var.set_type("string")
                return
        value_type = inter.get_type(self.args[1])
        if value_type is None:
            var.set_value(None)
            return
        elif value_type == "int":
            var.set_value("int")
        elif value_type == "bool":
            var.set_value("bool")
        elif value_type == "nil":
            var.set_value("nil")
        elif value_type == "string":
            var.set_value("string")
        else:
            exit(53)
        var.set_type("string")


class Label(Instruction):
    def __init__(self, args, order):
        super().__init__("LABEL", args, order)

    def execute(self, inter: Interpret):
        return


class Jump(Instruction):
    def __init__(self, args, order):
        super().__init__("JUMP", args, order)

    def execute(self, inter: Interpret, line):
        return inter.find_label(self.args[0].value)


class JumpIfEq(Instruction):
    def __init__(self, args, order):
        super().__init__("JUMPIFEQ", args, order)

    def execute(self, inter: Interpret, line):
        try:
            label = inter.find_label(self.args[0].value)
            value_1_type = inter.get_type(self.args[1])
            value_2_type = inter.get_type(self.args[2])
            value1 = inter.get_value(self.args[1].value)
            value2 = inter.get_value(self.args[2].value)
            if value_1_type == value_2_type:
                if value1 == value2:
                    return label
                return line + 1
            elif value_1_type == "nil" or value_2_type == "nil":
                return line + 1
            elif value_1_type is None or value_2_type is None:
                exit(56)
            else:
                exit(53)
        except IndexError:
            exit(58)


class JumpIfNEq(Instruction):
    def __init__(self, args, order):
        super().__init__("JUMPIFNEQ", args, order)

    def execute(self, inter: Interpret, line):
        try:
            label = inter.find_label(self.args[0].value)
            value_1_type = inter.get_type(self.args[1])
            value_2_type = inter.get_type(self.args[2])
            value1 = inter.get_value(self.args[1].value)
            value2 = inter.get_value(self.args[2].value)
            if value_1_type == value_2_type:
                if value1 != value2:
                    return label
                return line+1
            elif value_1_type == "nil" or value_2_type == "nil":
                return line + 1
            elif value_1_type is None or value_2_type is None:
                exit(56)
            else:
                exit(53)
        except IndexError:
            exit(58)


class Exit(Instruction):
    def __init__(self, args, order):
        super().__init__("EXIT", args, order)

    def execute(self, inter: Interpret):
        value_type = inter.get_type(self.args[0])
        if value_type == "int":
            value = inter.get_value(self.args[0].value)
            if 0 <= int(value) <= 49:
                exit(int(value))
            exit(57)
        else:
            exit(53)


class Dprint(Instruction):
    def __init__(self, args, order):
        super().__init__("DPRINT", args, order)

    def execute(self, inter: Interpret):
        value = inter.get_value(self.args[0].value)
        stderr.write(value)


class Break(Instruction):
    def __init__(self, args, order):
        super().__init__("BREAK", args, order)

    def execute(self, inter: Interpret, num_inst):
        stderr.write("pozice v kódu: " + str(self.order) + "\n")
        stderr.write("počet vykonaných instrukcí: " + str(num_inst+1) + "\n")
        stderr.write("globální rámce: \n")
        for gf in inter.global_frame.variables:
            stderr.write("  " + str(gf.name) + "\n")
        stderr.write("lokální rámce: \n")
        if bool(inter.local_frame):
            for lf in inter.local_frame[len(inter.local_frame)-1].variables:
                stderr.write("  " + str(lf.name) + "\n")
        stderr.write("dočasný rámce: \n")
        if inter.temporary_frame is None:
            stderr.write("  Neinicializován \n")
        else:
            for tf in inter.temporary_frame.variables:
                stderr.write("  " + str(tf.name) + "\n")


class Factory:
    """
    Factory class
    """
    @classmethod
    def make(cls, name: str, args, order):
        """
        make function is for decide what function to process
        :return instruction class
        """
        name = name.upper()
        if name == "MOVE":
            if len(args) != 2:
                exit(32)
            return Move(args, order)
        elif name == "CREATEFRAME":
            if len(args) != 0:
                exit(32)
            return CreateFrame(args, order)
        elif name == "PUSHFRAME":
            if len(args) != 0:
                exit(32)
            return PushFrame(args, order)
        elif name == "POPFRAME":
            if len(args) != 0:
                exit(32)
            return PopFrame(args, order)
        elif name == "DEFVAR":
            if len(args) != 1:
                exit(32)
            return Defvar(args, order)
        elif name == "CALL":
            if len(args) != 1:
                exit(32)
            return Call(args, order)
        elif name == "RETURN":
            if len(args) != 0:
                exit(32)
            return Return(args, order)
        elif name == "PUSHS":
            if len(args) != 1:
                exit(32)
            return Pushs(args, order)
        elif name == "POPS":
            if len(args) != 1:
                exit(32)
            return Pops(args, order)
        elif name == "ADD":
            if len(args) != 3:
                exit(32)
            return Add(args, order)
        elif name == "SUB":
            if len(args) != 3:
                exit(32)
            return Sub(args, order)
        elif name == "MUL":
            if len(args) != 3:
                exit(32)
            return Mul(args, order)
        elif name == "IDIV":
            if len(args) != 3:
                exit(32)
            return Idiv(args, order)
        elif name == "LT":
            if len(args) != 3:
                exit(32)
            return Lt(args, order)
        elif name == "GT":
            if len(args) != 3:
                exit(32)
            return Gt(args, order)
        elif name == "EQ":
            if len(args) != 3:
                exit(32)
            return Eq(args, order)
        elif name == "AND":
            if len(args) != 3:
                exit(32)
            return And(args, order)
        elif name == "OR":
            if len(args) != 3:
                exit(32)
            return Or(args, order)
        elif name == "NOT":
            if len(args) != 2:
                exit(32)
            return Not(args, order)
        elif name == "INT2CHAR":
            if len(args) != 2:
                exit(32)
            return Int2Char(args, order)
        elif name == "STRI2INT":
            if len(args) != 3:
                exit(32)
            return Stri2Int(args, order)
        elif name == "READ":
            if len(args) != 2:
                exit(32)
            return Read(args, order)
        elif name == "WRITE":
            if len(args) != 1:
                exit(32)
            return Write(args, order)
        elif name == "CONCAT":
            if len(args) != 3:
                exit(32)
            return Concat(args, order)
        elif name == "STRLEN":
            if len(args) != 2:
                exit(32)
            return Strlen(args, order)
        elif name == "GETCHAR":
            if len(args) != 3:
                exit(32)
            return Getchar(args, order)
        elif name == "SETCHAR":
            if len(args) != 3:
                exit(32)
            return Setchar(args, order)
        elif name == "TYPE":
            if len(args) != 2:
                exit(32)
            return Type(args, order)
        elif name == "LABEL":
            if len(args) != 1:
                exit(32)
            return Label(args, order)
        elif name == "JUMP":
            if len(args) != 1:
                exit(32)
            return Jump(args, order)
        elif name == "JUMPIFEQ":
            if len(args) != 3:
                exit(32)
            return JumpIfEq(args, order)
        elif name == "JUMPIFNEQ":
            if len(args) != 3:
                exit(32)
            return JumpIfNEq(args, order)
        elif name == "EXIT":
            if len(args) != 1:
                exit(32)
            return Exit(args, order)
        elif name == "DPRINT":
            if len(args) != 1:
                exit(32)
            return Dprint(args, order)
        elif name == "BREAK":
            if len(args) != 0:
                exit(32)
            return Break(args, order)
        else:
            exit(32)


h = Interpret()
h.start()

