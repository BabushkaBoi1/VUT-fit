package uml.ija.model;

/**
 * Trida reprezentuje klasifikator v diagramu
 *
 * @author Tomáš Polívka (xpoliv06)
 * @author Vojtěch Hájek (xhajek51)
 */
public class UMLClassifier extends Element {

    /**
     * Uzivatelsky definovan (soucasti diagramu).
     */
    private final boolean isUserDefined;

    /**
     * Vytvori instanci tridy Classifier.
     *
     * @param name Nazev klasifikatoru.
     */
    public UMLClassifier(String name) {
        super(name);
        this.isUserDefined = true;
    }

    /**
     * Vytvori instanci tridy Classifier.
     *
     * @param name          Nazev klasifikatoru.
     * @param isUserDefined Uzivatelsky definovan (soucasti diagramu).
     */
    public UMLClassifier(String name, boolean isUserDefined) {
        super(name);
        this.isUserDefined = isUserDefined;
    }

    /**
     * Tovarni metoda pro vytvoreni instance tridy Classifier pro zadane jmeno
     *
     * @param name Nazev klasifikatoru.
     * @return Vytvoreny klasifikator.
     */
    public static UMLClassifier forName(String name) {
        return new UMLClassifier(name, false);
    }

    /**
     * Zjišťuje, zda objekt reprezentuje klasifikator, ktery je modelovan uzivatelem v diagramu nebo ne.
     *
     * @return Pokud je klasifikator uzivatelsky definovan (je primo soucasti diagramu), vraci true. Jinak false.
     */
    public boolean isUserDefined() {
        return this.isUserDefined;
    }

    /**
     * Vraci retezec reprezentujici klasifikator v podobe "nazev(userDefined)", kde userDefined je true nebo false
     *
     * @return retezec reprezentujici klasifikator.
     */
    public String toString() {
        return this.name + "(" + this.isUserDefined + ")";
    }
}
