package uml.ija.model;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Trida reprezentuje operaci, ktera ma sve jmeno, navratovy typ a seznam argumentu.
 *
 * @author Tomáš Polívka (xpoliv06)
 * @author Vojtěch Hájek (xhajek51)
 */
public class UMLOperation extends UMLAttribute {

    private java.util.List<UMLAttribute> args;

    /**
     * Konstruktor pro vytvoreni operace s danym nazvem a navratovym typem
     *
     * @param name Nazev operace.
     * @param type Navratovy typ operace
     */
    public UMLOperation(String name, UMLClassifier type) {
        super(name, type);
        this.args = new ArrayList<>();
    }

    /**
     * Konstruktor pro vytvoreni operace s danym nazvem a navratovym typem
     *
     * @param name       Nazev operace.
     * @param type       Navratovy typ operace.
     * @param accessType Modifikátor přístupu.
     */
    public UMLOperation(String name, UMLClassifier type, UMLClassifier accessType) {
        super(name, type, accessType);
        this.args = new ArrayList<>();
    }

    /**
     * Tovární metoda pro vytvoření instance operace.
     *
     * @param name Název operace.
     * @param type Návratový typ operace.
     * @param args Seznam argumentů operace.
     * @return Objekt reprezentující operaci v diagramu UML.
     */
    public static UMLOperation create(String name, UMLClassifier type, UMLAttribute... args) {
        UMLOperation operation = new UMLOperation(name, type);
        operation.args.addAll(Arrays.asList(args));

        return operation;
    }

    /**
     * Tovární metoda pro vytvoření instance operace.
     *
     * @param name       Název operace.
     * @param type       Návratový typ operace.
     * @param accessType Modifikátor přístupu.
     * @param args       Seznam argumentů operace.
     * @return Objekt reprezentující operaci v diagramu UML.
     */
    public static UMLOperation create(String name, UMLClassifier type, UMLClassifier accessType, java.util.List<UMLAttribute> args) {
        UMLOperation operation = new UMLOperation(name, type, accessType);
        operation.args = args;

        return operation;
    }

    /**
     * Prida novy argument do seznamu argumentu.
     *
     * @param arg Vkladany argument.
     * @return Uspech operace - true, pokud se podarilo vlozit, jinak false.
     */
    public boolean addArgument(UMLAttribute arg) {
        return this.args.add(arg);
    }

    /**
     * Vraci seznam argumentu. Lze vyuzit pro zobrazeni.
     *
     * @return Seznam argumentu
     */
    public java.util.List<UMLAttribute> getArguments() {
        return this.args;
    }

    /**
     * Vraci retezec reprezentujici nazev atributu v podobe "nazevOperace(argumenty) : typ"
     *
     * @return Retezec reprezentujici atribut.
     */
    @Override
    public String toString() {
        String returnString = "";
        switch (this.accessType.getName()) {
            case "public":
                returnString += "+ ";
                break;
            case "protected":
                returnString += "# ";
                break;
            case "private":
                returnString += "- ";
                break;
            case "package":
                returnString += "~ ";
                break;
            default:
        }

        returnString += this.name + "( ";

        int count = this.args.size();

        int position = 1;
        for (UMLAttribute arg : this.args) {
            String argument = arg.getName() + " : " + arg.getType().getName();
            returnString = String.join("", returnString, argument);
            if (position != count) {
                returnString = String.join("", returnString, ", ");
            }
            position++;
        }
        returnString = String.join("", returnString, " )");
        returnString = String.join(" : ", returnString, this.type.getName());
        return returnString;
    }
}
