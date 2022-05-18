package uml.ija.model;

/**
 * Trida reprezentuje pojmenovany element (thing), ktery muze byt soucasti jakekoliv casti v diagramu.
 *
 * @author Tomáš Polívka (xpoliv06)
 * @author Vojtěch Hájek (xhajek51)
 */
public class Element {
    /**
     * Nazev elementu
     */
    public String name;

    /**
     * Styl elementu
     * 0 - default
     * 1 - chybejici zavislost
     * 2 - prekryti metody/atributu
     */
    public int textStyle;

    /**
     * Vytvori instanci se zadanym nazvem.
     *
     * @param name Nazev elementu
     */
    public Element(String name) {
        this.name = name;
        this.textStyle = 0;
    }

    /**
     * Vrati nazev elementu.
     *
     * @return Nazev elementu.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Prejmenuje element.
     *
     * @param name Novy nazev elementu.
     */
    public void rename(String name) {
        this.name = name;
    }

    /**
     * Vraci styl elementu
     *
     * @return
     */
    public int getTextStyle() {
        return textStyle;
    }

    /**
     * Nastaveni stylu elementu
     *
     * @param textStyle
     */
    public void setTextStyle(int textStyle) {
        this.textStyle = textStyle;
    }
}
