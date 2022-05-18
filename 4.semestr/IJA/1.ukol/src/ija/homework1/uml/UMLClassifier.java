package ija.homework1.uml;

public class UMLClassifier extends Element{
    protected boolean isUserDen;

    public UMLClassifier(java.lang.String name, boolean isUserDefined){
        super(name);
        this.isUserDen = isUserDefined;
    }

    public UMLClassifier(java.lang.String name){
        super(name);
    }

    public static UMLClassifier forName(java.lang.String name){
        return new UMLClassifier(name, false);
    }

    public boolean isUserDefined(){
        return this.isUserDen;
    }

    public java.lang.String toString(){
        return this.name+"("+this.isUserDen+")";
    }


}
