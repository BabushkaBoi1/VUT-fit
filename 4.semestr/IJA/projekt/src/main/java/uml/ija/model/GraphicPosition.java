package uml.ija.model;

/**
 * Trida pro praci s pozici
 *
 * @author Tomáš Polívka (xpoliv06)
 * @author Vojtěch Hájek (xhakej51)
 */
public class GraphicPosition {
    /**
     * Pozice x
     */
    protected double x;

    /**
     * Pozice y
     */
    protected double y;

    /**
     * Sirka
     */
    protected double width;

    /**
     * Vyska
     */
    protected double height;


    /**
     * Vytvori instanci vazby.
     */
    public GraphicPosition() {
    }

    /**
     * Ziskani pozice x
     */
    public double getX() {
        return this.x;
    }

    /**
     * Nastaveni pozice x
     *
     * @param value pozice
     */
    public void setX(double value) {
        this.x = value;
    }

    /**
     * Ziskani pozice y
     */
    public double getY() {
        return this.y;
    }

    /**
     * Nastaveni pozice y
     *
     * @param value pozice
     */
    public void setY(double value) {
        this.y = value;
    }

    /**
     * Ziskani vysky
     */
    public double getHeight() {
        return this.height;
    }

    /**
     * Nastaveni vysky
     *
     * @param value hodnota
     */
    public void setHeight(double value) {
        this.height = value;
    }

    /**
     * Ziskani sirky
     */
    public double getWidth() {
        return this.width;
    }

    /**
     * Nastaveni sirky
     *
     * @param value hodnota
     */
    public void setWidth(double value) {
        this.width = value;
    }
}
