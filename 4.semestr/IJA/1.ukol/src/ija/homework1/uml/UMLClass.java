package ija.homework1.uml;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class UMLClass extends UMLClassifier{

    protected boolean isAbstract;
    protected java.util.List<UMLAttribute> attr;

    public UMLClass(java.lang.String name){
        super(name, true);
        this.attr = new ArrayList<>();

    }

    public boolean isAbstract(){
        return this.isAbstract;
    }

    public void setAbstract(boolean isAbstract){
        this.isAbstract = isAbstract;
    }

    public boolean addAttribute(UMLAttribute attr){
        return this.attr.add(attr);
    }

    public int getAttrPosition(UMLAttribute attr){
        return this.attr.indexOf(attr);
    }

    public int moveAttrAtPosition(UMLAttribute attr, int pos){
        if(this.attr.contains(attr)){
            this.attr.remove(attr);
            this.attr.add(pos, attr);
            return 0;
        }
        return -1;
    }

    public java.util.List<UMLAttribute> getAttributes(){
        return Collections.unmodifiableList(this.attr);
    }
}
