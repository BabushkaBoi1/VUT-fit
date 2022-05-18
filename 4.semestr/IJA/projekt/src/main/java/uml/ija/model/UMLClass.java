package uml.ija.model;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Trida (jeji instance) reprezentuje model tridy z jazyka UML
 *
 * @author Tomáš Polívka (xpoliv06)
 * @author Vojtěch Hájek (xhajek51)
 */
public class UMLClass extends UMLClassifier {

    /**
     * Priznak, zda je trida abstraktni
     */
    private boolean isAbstract;

    /**
     * Seznam atributu
     */
    private final java.util.List<UMLAttribute> attributes;

    /**
     * Seznam operaci
     */
    private final java.util.List<UMLOperation> operations;

    /**
     * Trida ze ktere dedi trida dedi
     */
    private UMLClass extendFrom;

    /**
     * Pozice
     */
    private GraphicPosition position;

    /**
     * Pozice
     */
    private GraphicPosition sequencePosition;

    /**
     * Konstruktor tridy UMLClass
     *
     * @param name nazev tridy
     */
    public UMLClass(String name) {
        super(name, true);
        this.attributes = new ArrayList<>();
        this.operations = new ArrayList<>();
        this.position = new GraphicPosition();
    }

    /**
     * Test, zda objekt reprezentuje model abstraktni tridy.
     *
     * @return bool Vraci priznak, zda je trida abstraktni
     */
    public boolean isAbstract() {
        return this.isAbstract;
    }

    /**
     * Zmeni informaci objektu, zda reprezentuje abstraktni tridu
     *
     * @param isAbstract Priznak, zda je trida abstraktni
     */
    public void setAbstract(boolean isAbstract) {
        this.isAbstract = isAbstract;
    }

    /**
     * Vlozi atribut do modelu UML tridy.
     *
     * @param attr Vkladany atribut
     * @return Uspech akce (pokud se podarilo vlozit, vraci true, jinak false).
     */
    public boolean addAttribute(UMLAttribute attr) {
        if (this.attributes.contains(attr)) {
            return false;
        }
        return this.attributes.add(attr);
    }

    /**
     * Vlozi operaci do modelu UML tridy.
     *
     * @param operation Vkladana operace
     * @return Uspech akce (pokud se podarilo vlozit, vraci true, jinak false).
     */
    public boolean addOperation(UMLOperation operation) {
        if (this.operations.contains(operation)) {
            return false;
        }
        return this.operations.add(operation);
    }

    /**
     * Vraci pozici atributu v seznamu atributu.
     *
     * @param attr Hledany atribut.
     * @return Pozice atributu. (V pripade neúspechu -1)
     */
    public int getAttrPosition(UMLAttribute attr) {
        return this.attributes.indexOf(attr);
    }

    /**
     * Presune pozici atributu na nove zadanou.
     *
     * @param attr Presunovany atribut.
     * @param pos  Nova pozice.
     * @return int Úspech operace.
     */
    public int moveAttrAtPosition(UMLAttribute attr, int pos) {
        int index = this.attributes.indexOf(attr);
        if (index == -1) {
            return -1;
        }

        this.attributes.remove(index);
        this.attributes.add(pos, attr);
        return 0;
    }

    /**
     * Smaze atribut ze tridy.
     *
     * @param attr Mazany atribut.
     * @return int Úspech operace.
     */
    public boolean removeAttr(UMLAttribute attr) {
        int index = this.attributes.indexOf(attr);
        if (index == -1) {
            return false;
        }

        this.attributes.remove(index);
        return true;
    }

    /**
     * Smaze operaci ze tridy.
     *
     * @param operation Mazana operace.
     * @return int Uspech operace.
     */
    public boolean removeOperation(UMLOperation operation) {
        int index = this.operations.indexOf(operation);
        if (index == -1) {
            return false;
        }

        this.operations.remove(index);
        return true;
    }

    /**
     * Vraci atribut ze seznamu atributu.
     *
     * @param name Nazev atributu.
     * @return Vraci atribut (V pripade neúspechu null)
     */
    public UMLAttribute getAttribute(String name) {
        for (UMLAttribute attr : this.attributes) {
            if (attr.getName().equals(name)) {
                return attr;
            }
        }
        return null;
    }

    public UMLClass getExtendFrom() {
        return extendFrom;
    }

    public void setExtendFrom(UMLClass extendFrom) {
        this.extendFrom = extendFrom;
    }

    /**
     * Vraci operaci ze seznamu operaci.
     *
     * @param name Nazev operace.
     * @return Vraci operaci (V pripade neúspechu null)
     */
    public UMLOperation getOperation(String name) {
        for (UMLOperation operation : this.operations) {
            if (operation.getName().equals(name)) {
                return operation;
            }
        }
        return null;
    }

    /**
     * Vraci operaci ze seznamu operaci.
     *
     * @param name Nazev operace.
     * @return Vraci operaci (V pripade neúspechu null)
     */
    public UMLOperation getOperationByFullName(String name) {
        for (UMLOperation operation : this.operations) {
            if (operation.toString().equals(name)) {
                return operation;
            }
        }
        return null;
    }

    /**
     * Vraci nemodifikovatelny seznam atributu
     *
     * @return Nemodifikovatelny seznam atributu.
     */
    public java.util.List<UMLAttribute> getAttributes() {
        return Collections.unmodifiableList(this.attributes);
    }

    /**
     * Vraci nemodifikovatelny seznam operaci
     *
     * @return Nemodifikovatelny seznam operaci.
     */
    public java.util.List<UMLOperation> getOperations() {
        return Collections.unmodifiableList(this.operations);
    }


    /**
     * Vraci pozici tridy
     *
     * @return pozici.
     */
    public GraphicPosition getPosition() {
        return this.position;
    }

    /**
     * Ulozi pozici tridy
     */
    public void setPosition(GraphicPosition position) {
        this.position = position;
    }

    /**
     * Ziska pozice pro sekvencni diagram
     *
     * @return Pozici
     */
    public GraphicPosition getSequencePosition() {
        return sequencePosition;
    }

    /**
     * Nastavi pozice pro sekvencni diagram
     *
     * @param sequencePosition pozice
     */
    public void setSequencePosition(GraphicPosition sequencePosition) {
        this.sequencePosition = sequencePosition;
    }
}
