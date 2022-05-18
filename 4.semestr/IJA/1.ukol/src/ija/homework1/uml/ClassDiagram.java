package ija.homework1.uml;

import java.util.ArrayList;
import java.util.Objects;

public class ClassDiagram extends Element{
    protected java.util.List<UMLClassifier> classifiers;
    protected java.util.List<UMLClass> classes;

    public ClassDiagram(java.lang.String name){
        super(name);
        this.classifiers = new ArrayList<>();
        this.classes = new ArrayList<>();
    }

    public UMLClass createClass(java.lang.String name){
        for (UMLClass class0 : this.classes){
            if(Objects.equals(class0.name, name)){
                return null;
            }
        }
        UMLClass a =  new UMLClass(name);
        this.classes.add(a);
        this.classifiers.add(a);
        return a;
    }

    public UMLClassifier classifierForName(java.lang.String name){
        for(UMLClassifier classifier : this.classifiers){
            if(Objects.equals(classifier.name, name)){
                return classifier;
            }
        }
        UMLClassifier a =  new UMLClassifier(name);
        this.classifiers.add(a);
        return a;
    }

    public UMLClassifier findClassifier(java.lang.String name) {
        for (UMLClassifier classifier: this.classifiers){
            if(Objects.equals(classifier.name, name)){
                return classifier;
            }
        }
        return null;
    }



}
