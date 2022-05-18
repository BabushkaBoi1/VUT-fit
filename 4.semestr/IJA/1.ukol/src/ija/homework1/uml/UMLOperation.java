package ija.homework1.uml;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class UMLOperation extends UMLAttribute{
    protected java.util.List<UMLAttribute> args;

    public UMLOperation(java.lang.String name, UMLClassifier type){
        super(name, type);
        this.args = new ArrayList<>();
    }

    public static UMLOperation create(java.lang.String name, UMLClassifier type, UMLAttribute... args){
        UMLOperation newOperation = new UMLOperation(name, type);
        newOperation.args.addAll(Arrays.asList(args));
        return newOperation;
    }

    public boolean addArgument(UMLAttribute arg){
        return this.args.add(arg);
    }

    public java.util.List<UMLAttribute> getArguments(){
        return Collections.unmodifiableList(this.args);
    }
}
