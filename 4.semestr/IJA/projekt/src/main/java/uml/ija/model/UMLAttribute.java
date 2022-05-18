package uml.ija.model;

import java.util.List;

/**
 * Trida reprezentuje atribut, ktery ma sve jmeno a typ
 *
 * @author Tomáš Polívka (xpoliv06)
 * @author Vojtěch Hájek (xhajek51)
 */
public class UMLAttribute extends Element {

    /**
     * Typ atributu
     */
    protected UMLClassifier type;

    /**
     * Modifikator pristupu.
     */
    protected UMLClassifier accessType;

    protected boolean isList = false;

    /**
     * Vytvori instanci atributu.
     *
     * @param name Nazev atributu.
     * @param type Typ atributu.
     */
    public UMLAttribute(String name, UMLClassifier type) {
        super(name);
        this.type = type;
    }

    /**
     * Vytvori instanci atributu s modifikatorem pristupu.
     *
     * @param name       Nazev atributu.
     * @param type       Typ atributu.
     * @param accessType Modifikator pristupu.
     */
    public UMLAttribute(String name, UMLClassifier type, UMLClassifier accessType) {
        super(name);
        this.type = type;
        this.accessType = accessType;
    }

    /**
     * Poskytuje informaci o typu atributu.
     *
     * @return Typ atributu.
     */
    public UMLClassifier getType() {
        return this.type;
    }

    /**
     * Poskytuje informaci o typu modifikatoru pristupu.
     *
     * @return Typ atributu.
     */
    public UMLClassifier getAccessType() {
        return this.accessType;
    }

    /**
     * Vrací řetězec reprezentující stav atributu v podobě "nazev:typ"
     *
     * @return Řetězec reprezentující atribut.
     */
    public String toString() {
        return this.name + ":" + this.type;
    }


    /**
     * Vrati priznak zda se jedna
     * @return
     */
    public boolean isList() {
        return isList;
    }

    public void setList(boolean list) {
        isList = list;
    }

    /**
     * Vrací řetězec reprezentující stav atributu v podobě "nazev:typ"
     *
     * @return Řetězec reprezentující atribut.
     */
    public String toStringClass() {
        String returnString = "";
        if (this.accessType != null) {
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
        }

        if (!this.isList)
        {
            returnString = returnString + this.name + " : " + this.type.getName();
        }
        else
        {
            returnString = returnString + this.name + " : List<" + this.type.getName() + ">";
        }

        return returnString;
    }
}
