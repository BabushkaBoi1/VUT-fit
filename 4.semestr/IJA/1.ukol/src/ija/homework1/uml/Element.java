package ija.homework1.uml;

public class Element extends java.lang.Object{
    public java.lang.String name;

    public Element(java.lang.String name){
        this.name = name;
    }

    public java.lang.String getName(){
        return this.name;
    }

    public void rename(java.lang.String newName){
        this.name = newName;
    }
}
