package uml.ija.model;

/**
 * Trida pro zpravu v sekvečním diagramu
 *
 * @author Tomáš Polívka (xpoliv06)
 * @author Vojtěch Hájek (xhajek51)
 */
public class SequenceMessage {

    /**
     * Styl elementu
     * 0 - default
     * 1 - chybejici zavislost
     * 2 - prekryti metody/atributu
     */
    public int textStyle;

    /**
     * Nazev klasifikatoru ze ktereho jde vazba
     */
    protected UMLClass from;

    /**
     * Nazev klasifikatoru do ktereho jde vazba
     */
    protected UMLClass to;

    /**
     * Typ vazby
     */
    protected String typeOfMessage = "";

    /**
     * Název operace
     * např. "Create UMLClass"
     */
    protected String operation = "";
    /**
     * Zprava
     */
    protected String message = "";

    /**
     * Kontruktor pro vytvoreni nove instance
     *
     * @param from          Trida odkud
     * @param to            Trida kam
     * @param typeOfMessage Typ vazby
     * @param operation     Operace
     */
    public SequenceMessage(UMLClass from, UMLClass to, String typeOfMessage, String operation) {
        this.from = from;
        this.to = to;
        this.typeOfMessage = typeOfMessage;
        this.operation = operation;
    }

    /**
     * Funkce pro ziskani zpravy
     *
     * @return zpravu
     */
    public String getMessage() {
        return message;
    }

    /**
     * Funkce pro nastaveni zpravy
     *
     * @param message zprava
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Funkce pro ziskani tridy
     *
     * @return Trida odkud
     */
    public UMLClass getFrom() {
        return from;
    }

    /**
     * Funkce pro nastaveni tridy odkud
     *
     * @param from Trida odkud
     */
    public void setFrom(UMLClass from) {
        this.from = from;
    }

    /**
     * Funkce pro ziskani tridy kam
     *
     * @return Trida kam
     */
    public UMLClass getTo() {
        return to;
    }

    /**
     * Funkce pro ziskani tridy kam
     *
     * @param to Trida kam
     */
    public void setTo(UMLClass to) {
        this.to = to;
    }

    /**
     * Funkce pro ziskani nazvu operace
     *
     * @return nazev operace
     */
    public String getOperation() {
        return operation;
    }

    /**
     * Nastavi nazev operace
     *
     * @param operation Operace
     */
    public void setOperation(String operation) {
        this.operation = operation;
    }

    /**
     * Funkce pro ziskani typu zpravy
     *
     * @return typ zpravy
     */
    public String getTypeOfMessage() {
        return typeOfMessage;
    }

    /**
     * Funkce pro nastaveni typu zpravy
     *
     * @param typeOfMessage typ zpravy
     */
    public void setTypeOfMessage(String typeOfMessage) {
        this.typeOfMessage = typeOfMessage;
    }

    /**
     * Vraci styl elementu
     *
     * @return styl
     */
    public int getTextStyle() {
        return textStyle;
    }

    /**
     * Nastaveni stylu elementu
     *
     * @param textStyle styl elementu
     */
    public void setTextStyle(int textStyle) {
        this.textStyle = textStyle;
    }
}
