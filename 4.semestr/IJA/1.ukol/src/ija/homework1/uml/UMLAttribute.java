package ija.homework1.uml;

public class UMLAttribute extends Element{
    public UMLClassifier type;

    public UMLAttribute(java.lang.String name, UMLClassifier type){
        super(name);
        this.type = type;
    }

    public UMLClassifier getType(){
        return this.type;
    }

    public java.lang.String toString(){
        return this.name+":"+this.type;
    }

}
