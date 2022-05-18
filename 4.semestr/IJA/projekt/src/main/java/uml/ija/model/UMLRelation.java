package uml.ija.model;

/**
 * Trida pro praci s vazbami
 *
 * @author Tomáš Polívka (xpoliv06)
 * @author Vojtěch Hájek (xhakej51) Trida pro ulozeni vazeb mezi jednotlivymi tridami
 */
public class UMLRelation {
    /**
     * Nazev klasifikatoru ze ktereho jde vazba
     */
    protected UMLClass from;

    /**
     * Hodnota, ktera se nachazi u vazby
     * Napr. '*'
     */
    protected String valueFrom = "";

    /**
     * Nazev klasifikatoru do ktereho jde vazba
     */
    protected UMLClass to;

    /**
     * Hodnota, ktera se nachazi u vazby
     * Napr. '1 ... n'
     */
    protected String valueTo = "";

    /**
     * Typ vazby
     * (Association, Aggregation, Composition, Generalization)
     */
    protected UMLClassifier typeOfRelation;

    /**
     * Vytvori instanci vazby.
     */
    public UMLRelation() {

    }

    /**
     * Vytvori instanci vazby.
     */
    public UMLRelation(UMLClass from, UMLClass to, UMLClassifier typeOfRelation) {
        this.from = from;
        this.to = to;
        this.typeOfRelation = typeOfRelation;
    }

    /**
     * Nastaveni vazby z jake tridy pochazi
     *
     * @return klasifikator class from
     */
    public UMLClass getClassFrom() {
        return this.from;
    }

    /**
     * Nastaveni vazby z jake tridy pochazi
     *
     * @param from Nazev klasifikatoru odkud jde vazba
     */
    public void setClassFrom(UMLClass from) {
        this.from = from;
    }

    /**
     * Nastaveni vazby do jake jde tridy
     *
     * @return klasifikator class to
     */
    public UMLClass getClassTo() {
        return this.to;
    }

    /**
     * Nastaveni vazby do jake jde tridy
     *
     * @param to Nazev klasifikatoru kam jde vazba
     */
    public void setClassTo(UMLClass to) {
        this.to = to;
    }

    /**
     * Nastaveni hodnoty pro vazbu (napr. '1 ... n')
     *
     * @return hodnotu value to
     */
    public String getValueTo() {
        return this.valueTo;
    }

    /**
     * Nastaveni hodnoty pro vazbu (napr. '1 ... n')
     *
     * @param toValue Hodnota
     */
    public void setValueTo(String toValue) {
        this.valueTo = toValue;
    }

    /**
     * Nastaveni hodnoty pro vazbu (napr. '*')
     *
     * @return hodnotu value from
     */
    public String getValueFrom() {
        return this.valueFrom;
    }

    /**
     * Nastaveni hodnoty pro vazbu (napr. '*')
     *
     * @param fromValue Hodnota
     */
    public void setValueFrom(String fromValue) {
        this.valueFrom = fromValue;
    }

    /**
     * Nastaveni typu vazby (napr. 'Generalizace')
     *
     * @return klasifikator type of relation
     */
    public UMLClassifier getTypeOfRelation() {
        return this.typeOfRelation;
    }

    /**
     * Nastaveni typu vazby (napr. 'Generalizace')
     *
     * @param typeOfRelation Druh vazby
     */
    public void setTypeOfRelation(UMLClassifier typeOfRelation) {
        this.typeOfRelation = typeOfRelation;
    }
}
