package uml.ija.model;

import java.util.ArrayList;

/**
 * Hlavni trida pro praci s diagramem trid
 *
 * @author Tomáš Polívka (xpoliv06)
 * @author Vojtěch Hájek (xhajek51)
 */
public class ClassDiagram extends Element {
    /**
     * Seznam klasifikatoru pro modifikatory pristupnosti
     */
    private final java.util.List<UMLClassifier> accessClassifiers;

    /**
     * Seznam klasifikatoru
     */
    private final java.util.List<UMLClassifier> classifiers;

    /**
     * Seznam dostupnych vazeb
     */
    private final java.util.List<UMLClassifier> relationsClassifiers;

    /**
     * Seznam trid
     */
    private final java.util.List<UMLClass> classes;

    /**
     * Seznam vazeb mezi tridy
     */
    private final java.util.List<UMLRelation> relations;

    /**
     * Pole sekvencnich diagramu
     */
    private final java.util.List<SequenceDiagram> sequenceDiagrams;

    /**
     * Konstruktor pro vytvoreni instance diagramu. Kazdy diagram má svuj nazev
     *
     * @param name Nazev diagramu.
     */
    public ClassDiagram(String name) {
        super(name);
        this.classes = new ArrayList<>();
        this.classifiers = new ArrayList<>();
        this.accessClassifiers = new ArrayList<>();
        this.relationsClassifiers = new ArrayList<>();
        this.relations = new ArrayList<>();
        this.sequenceDiagrams = new ArrayList<>();

        this.initBasicData();
    }

    private void initBasicData() {
        this.classifiers.add(UMLClass.forName("string"));
        this.classifiers.add(UMLClassifier.forName("void"));
        this.classifiers.add(UMLClassifier.forName("int"));
        this.classifiers.add(UMLClassifier.forName("bool"));

        this.accessClassifiers.add(UMLClassifier.forName("private"));
        this.accessClassifiers.add(UMLClassifier.forName("protected"));
        this.accessClassifiers.add(UMLClassifier.forName("package"));
        this.accessClassifiers.add(UMLClassifier.forName("public"));

        this.relationsClassifiers.add(UMLClassifier.forName("association"));
        this.relationsClassifiers.add(UMLClassifier.forName("aggregation"));
        this.relationsClassifiers.add(UMLClassifier.forName("composition"));
        this.relationsClassifiers.add(UMLClassifier.forName("generalization"));
    }

    /**
     * Vytvori instanci UML tridy a vlozi ji do diagramu
     *
     * @param name Nazev vytvarene tridy.
     * @return Objekt (instance) reprezentujici tridu
     */
    public UMLClass createClass(String name) {
        for (UMLClass class_find : this.classes) {
            if (class_find.getName().equals(name)) {
                return null;
            }
        }
        UMLClass class_add = new UMLClass(name);
        this.classes.add(class_add);
        this.classifiers.add(class_add);
        return class_add;

    }

    /**
     * Funkce na pridani nove vazby
     *
     * @param relation Vazba, ktera se ma pridat do listu
     */
    public void addRelation(UMLRelation relation) {
        if (relation.getTypeOfRelation().getName().equals("generalization")) {
            UMLClass umlClassFrom = relation.getClassFrom();
            umlClassFrom.setExtendFrom(relation.getClassTo());
        }
        this.relations.add(relation);
    }

    /**
     * Funkce pro smazani relace
     *
     * @param relation Vazba, ktera se ma smazat z listu
     */
    public void deleteRelation(UMLRelation relation) {
        if (relation.getTypeOfRelation().getName().equals("generalization")) {
            UMLClass umlClassTo = relation.getClassTo();
            umlClassTo.setExtendFrom(null);

            // Nastaveni vsech atributu a operaci na vychozi styl
            for (UMLOperation umlOperation : umlClassTo.getOperations()) {
                if (umlOperation.getTextStyle() != 1) {
                    umlOperation.setTextStyle(0);
                }
            }

            for (UMLAttribute umlAttribute : umlClassTo.getAttributes()) {
                if (umlAttribute.getTextStyle() != 1) {
                    umlAttribute.setTextStyle(0);
                }
            }
        }
        this.relations.remove(relation);
    }

    /**
     * Funkce na pridani nove tridy
     *
     * @param umlClass Trida, ktera se ma pridat do listu
     */
    public void addClass(UMLClass umlClass) {
        this.classes.add(umlClass);
    }

    /**
     * Funkce na pridani noveho klasifikatoru
     *
     * @param classifier Klasifikator, ktery se ma pridat do listu
     */
    public void addClassifier(UMLClassifier classifier) {
        if (findClassifier(classifier.getName()) == null) {
            this.classifiers.add(classifier);
        }
    }

    /**
     * Funkce na pridani noveho klasifikatoru
     *
     * @param classifier Klasifikator, ktery se ma pridat do listu
     */
    public void addRelationClassifier(UMLClassifier classifier) {
        if (findRelationsClassifier(classifier.getName()) == null) {
            this.relationsClassifiers.add(classifier);
        }
    }

    /**
     * Funkce na pridani noveho modifikatoru
     *
     * @param accessClassifier Modifikator, ktery se ma pridat do listu
     */
    public void addAccessClassifier(UMLClassifier accessClassifier) {
        this.accessClassifiers.add(accessClassifier);
    }

    /**
     * Vyhleda v diagramu klasifikator podle nazvu. Pokud neexistuje, vytvori instanci tridy Classifier reprezentujici klasifikator.
     *
     * @param name Nazev klasifikatoru.
     * @return Nalezeny, prip. vytvoreny, klasifikator.
     */
    public UMLClassifier classifierForName(String name) {
        for (UMLClassifier classifier : this.classifiers) {
            if (classifier.getName().equals(name)) {
                return classifier;
            }
        }
        UMLClassifier classifier = UMLClassifier.forName(name);
        this.classifiers.add(classifier);
        return classifier;
    }

    /**
     * Vyhleda v diagramu klasifikator pristupu podle nazvu. Pokud neexistuje, vytvori instanci tridy Classifier reprezentujici klasifikator.
     *
     * @param name Nazev klasifikatoru.
     * @return Nalezeny, prip. vytvoreny, klasifikator (modifikator pristupu).
     */
    public UMLClassifier accessClassifierForName(String name) {
        for (UMLClassifier classifier : this.accessClassifiers) {
            if (classifier.getName().equals(name)) {
                return classifier;
            }
        }
        UMLClassifier classifier = UMLClassifier.forName(name);
        this.accessClassifiers.add(classifier);
        return classifier;
    }

    /**
     * Vyhleda v diagramu klasifikator podle nazvu
     *
     * @param name Nazev klasifikatoru
     * @return Nalezeny klasifikator nebo null
     */
    public UMLClassifier findClassifier(String name) {
        for (UMLClassifier classifier : this.classifiers) {
            if (classifier.getName().equals(name)) {
                return classifier;
            }
        }
        return null;
    }

    /**
     * Vyhleda v diagramu modifikator pristupnosti podle nazvu
     *
     * @param name Nazev modifikatoru
     * @return Nalezeny modifikator nebo null
     */
    public UMLClassifier findAccessClassifier(String name) {
        for (UMLClassifier classifier : this.accessClassifiers) {
            if (classifier.getName().equals(name)) {
                return classifier;
            }
        }
        return null;
    }

    /**
     * Vyhleda v diagramu typ vazby nazvu
     *
     * @param name Nazev typ vazby
     * @return Nalezeny typ nebo null
     */
    public UMLClassifier findRelationsClassifier(String name) {
        for (UMLClassifier classifier : this.relationsClassifiers) {
            if (classifier.getName().equals(name)) {
                return classifier;
            }
        }
        return null;
    }

    /**
     * Vyhleda v diagramu tridu podle nazvu
     *
     * @param name Nazev tridy
     * @return Nalezena trida nebo null
     */
    public UMLClass findClass(String name) {
        for (UMLClass umlClass : this.classes) {
            if (umlClass.getName().equals(name)) {
                return umlClass;
            }
        }
        return null;
    }

    /**
     * Funkce pro smazani tridy
     *
     * @param name nazev tridy
     * @return true - pokud se smazani povedlo
     */
    public boolean deleteClass(String name) {

        for (UMLRelation umlRelation : new ArrayList<>(this.getRelations())) {
            if (umlRelation.getClassFrom() == this.findClass(name) || umlRelation.getClassTo() == this.findClass(name)) {
                this.relations.remove(umlRelation);
            }
        }

        for (SequenceDiagram sequenceDiagram : new ArrayList<>(this.getSequenceDiagrams())) {
            sequenceDiagram.deleteClass(this.findClass(name));
        }

        boolean remove1 = this.classes.removeIf(umlClass -> umlClass.getName().equals(name));
        boolean remove2 = this.classifiers.removeIf(umlClassifier -> umlClassifier.getName().equals(name));

        return remove1 && remove2;
    }


    /**
     * Zkontroluje diagram a jednotlivé závislosti
     *
     * @return Nalezena trida nebo null
     */
    public boolean checkDiagram() {
        boolean diagramIsOkey = true;
        boolean isExist = false;
        // Prochazeni vsech trid
        for (UMLClass umlClass : this.classes) {
            if (umlClass.getExtendFrom() != null) {
                this.checkInheritanceHierarchy(umlClass, this.findClass(umlClass.getExtendFrom().getName()));
            }

            // Pruchod vsech atributu a kontrola typu
            for (UMLAttribute umlAttribute : umlClass.getAttributes()) {
                isExist = false;
                for (UMLClassifier classifier : this.classifiers) {
                    if (umlAttribute.getType().getName().equals(classifier.getName())) {
                        isExist = true;
                        break;
                    }
                }
                if (!isExist) {
                    diagramIsOkey = false;
                    umlAttribute.setTextStyle(1);
                } else {
                    if (umlAttribute.getTextStyle() != 2) {
                        umlAttribute.setTextStyle(0);
                    }
                }
            }

            // Pruchod vsech operaci
            for (UMLOperation umlOperation : umlClass.getOperations()) {
                boolean isAttType = true;
                // Pruchod argumentu funkce a kontrola vstupnich typu funkce
                for (UMLAttribute umlAttribute : umlOperation.getArguments()) {
                    isExist = false;
                    for (UMLClassifier classifier : this.classifiers) {
                        if (umlAttribute.getType().getName().equals(classifier.getName())) {
                            isExist = true;
                            break;
                        }
                    }
                    if (!isExist) {
                        isAttType = false;
                        umlAttribute.setTextStyle(1);
                    } else {
                        if (umlAttribute.getTextStyle() != 2) {
                            umlAttribute.setTextStyle(0);
                        }
                    }
                }
                if (!isAttType) {
                    diagramIsOkey = false;
                    umlOperation.setTextStyle(1);
                } else {
                    if (umlOperation.getTextStyle() != 2) {
                        umlOperation.setTextStyle(0);
                    }
                }

                isExist = false;
                // Pruchod operaci a kontrola vracenych typu
                for (UMLClassifier classifier : this.classifiers) {
                    if (umlOperation.getType().getName().equals(classifier.getName())) {
                        isExist = true;
                        break;
                    }
                }
                if (!isExist || !isAttType) {
                    diagramIsOkey = false;
                    umlOperation.setTextStyle(1);
                } else {
                    if (umlOperation.getTextStyle() != 2) {
                        umlOperation.setTextStyle(0);
                    }
                }
            }
        }

        return diagramIsOkey;
    }

    /**
     * Prejmenuje vsechny zavislosti
     *
     * @param oldName Stare jmeno tridy
     * @param newName Nove jmeno tridy
     */
    public void renameClassInDiagram(String oldName, String newName) {
        UMLClassifier classifierToRename = this.findClassifier(oldName);
        classifierToRename.rename(newName);

        UMLClass classToRename = this.findClass(oldName);
        classToRename.rename(newName);
    }

    private void checkInheritanceHierarchy(UMLClass umlClass, UMLClass extendFrom) {
        if (extendFrom == null || umlClass == null) {
            return;
        }

        // Pruchod vsech operaci a pripadne nastaveni prekryti
        for (UMLOperation umlOperation : umlClass.getOperations()) {
            boolean isExist = false;
            for (UMLOperation umlOperation1 : extendFrom.getOperations()) {
                // Pokud je operace private tak se přeskakuje
                if (umlOperation1.getAccessType().getName().equals("private")) {
                    continue;
                }
                // Pri shode jmen se zkontroluje pocet argumentu
                if (umlOperation.getName().equals(umlOperation1.getName())) {
                    if (umlOperation.getArguments().size() == umlOperation1.getArguments().size()) {
                        // Nastaveni stylu na prekryti
                        isExist = true;
                        break;
                    }
                }
            }
            if (isExist) {
                umlOperation.setTextStyle(2);
            } else {
                umlOperation.setTextStyle(0);
            }
        }

        // Pruchod vsech operaci a pripadne nastaveni prekryti
        for (UMLAttribute umlAttribute : umlClass.getAttributes()) {
            for (UMLAttribute umlAttribute1 : extendFrom.getAttributes()) {
                // Pokud je atribut private tak se přeskakuje
                if (umlAttribute1.getAccessType().getName().equals("private")) {
                    continue;
                }

                // Pri shode jmen atributu se nastavi styl prekryti
                if (umlAttribute.getName().equals(umlAttribute1.getName())) {
                    umlAttribute.setTextStyle(2);
                    break;
                } else {
                    umlAttribute.setTextStyle(0);
                }
            }
        }

        if (extendFrom.getExtendFrom() != null) {
            this.checkInheritanceHierarchy(umlClass, extendFrom.getExtendFrom());
        }
    }

    /**
     * Vraci seznam trid. Lze vyuzit pro zobrazeni.
     *
     * @return seznam trid
     */
    public java.util.List<UMLClass> getClasses() {
        return this.classes;
    }

    /**
     * Vraci seznam klasifikatoru. Lze vyuzit pro zobrazeni.
     *
     * @return seznam klasifikatoru
     */
    public java.util.List<UMLClassifier> getClassifiers() {
        return this.classifiers;
    }

    /**
     * Vraci seznam klasifikatoru. Lze vyuzit pro zobrazeni.
     *
     * @return seznam klasifikatoru
     */
    public java.util.List<UMLClassifier> getAccessClassifiers() {
        return this.accessClassifiers;
    }

    /**
     * Vraci seznam klasifikatoru. Lze vyuzit pro zobrazeni.
     *
     * @return seznam klasifikatoru
     */
    public java.util.List<UMLClassifier> getRelationsClassifiers() {
        return this.relationsClassifiers;
    }

    /**
     * Vraci seznam vazeb. Lze vyuzit pro zobrazeni.
     *
     * @return seznam vazeb
     */
    public java.util.List<UMLRelation> getRelations() {
        return this.relations;
    }

    /**
     * Vraci seznam sekvecnich diagramu. Lze vyuzit pro zobrazeni.
     *
     * @return seznam sekvencich diagramu
     */
    public java.util.List<SequenceDiagram> getSequenceDiagrams() {
        return this.sequenceDiagrams;
    }

    /**
     * Prida sekvecni diagram.
     *
     * @param diagram novy sekvencni diagram
     */
    public void addSequenceDiagram(SequenceDiagram diagram) {
        this.sequenceDiagrams.add(diagram);
    }

    /**
     * Vytvori sekvecni diagram.
     *
     * @param name nazev sekvencniho diagramu
     */
    public void createSequenceDiagram(String name) {
        this.sequenceDiagrams.add(new SequenceDiagram(name));
    }

    /**
     * Odebere sekvecni diagram.
     *
     * @param diagram diagram pro odebrani
     */
    public void deleteSequenceDiagram(SequenceDiagram diagram) {
        this.sequenceDiagrams.remove(diagram);
    }

}
