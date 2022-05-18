package uml.ija.model;

import java.util.ArrayList;

/**
 * Trida pro sekvencni diagram
 *
 * @author Tomáš Polívka (xpoliv06)
 * @author Vojtěch Hájek (xhajek51)
 */
public class SequenceDiagram extends Element
{
    /**
     * Seznam trid
     */
    private java.util.List<UMLClass> classes;

    /**
     * Seznam zprav
     */
    private java.util.List<SequenceMessage> messages;

    /**
     * Seznam typu zprav
     */
    private java.util.List<String> typeOfMessages;

    /**
     * Vytvori instanci se zadanym nazvem.
     *
     * @param name Nazev elementu
     */
    public SequenceDiagram(String name) {
        super(name);
        this.classes = new ArrayList<>();
        this.messages = new ArrayList<>();
        this.typeOfMessages = new ArrayList<>();

        this.initData();
    }

    /**
     * Funkce pro inicializaci zakladnich dat
     */
    private void initData()
    {
        this.typeOfMessages.add("full");
        this.typeOfMessages.add("dashed");
    }

    /**
     * Funkce pro pridani zpravy
     *
     * @param message zprava
     */
    public void addMessage(SequenceMessage message)
    {
        this.messages.add(message);
    }

    /**
     * Funkce pro smazani zpravy
     *
     * @param message smaze zpravu
     */
    public void deleteMessage(SequenceMessage message)
    {
        this.messages.remove(message);
    }

    /**
     * Funkce pro ziskani zprav
     *
     * @return zpravy
     */
    public java.util.List<SequenceMessage> getMessages()
    {
        return this.messages;
    }

    /**
     * Funkce pro pridani tridy
     *
     * @param umlClass Trida
     */
    public void addClass(UMLClass umlClass)
    {
        this.classes.add(umlClass);
    }

    /**
     * Funkce pro ziskani typu zprav
     *
     * @return typ zprav
     */
    public java.util.List<String> getTypeOfMessages()
    {
        return this.typeOfMessages;
    }

    /**
     * Funkce pro ziskani trid
     *
     * @return tridy
     */
    public java.util.List<UMLClass> getClasses()
    {
        return this.classes;
    }

    /**
     * Vyhleda v diagramu tridu podle nazvu
     *
     * @param name Nazev tridy
     * @return Nalezena trida nebo null
     */
    public UMLClass findClass(String name){
        for (UMLClass umlClass : this.classes) {
            if (umlClass.getName().equals(name)) {
                return umlClass;
            }
        }
        return null;
    }

    /**
     * Funkce pro smazani uml tridy
     *
     * @param umlClass Uml trida
     */
    public void deleteClass(UMLClass umlClass)
    {
        for(SequenceMessage message :  new ArrayList<>(this.getMessages()))
        {
            if (message.getFrom() == umlClass || message.getTo() == umlClass)
            {
                this.deleteMessage(message);
            }
        }
        this.classes.remove(umlClass);
    }

    /**
     * Funkce pro kontrolu sekvencniho diagramu
     */
    public void checkSequenceDiagram()
    {
        for (SequenceMessage message : this.messages)
        {
            UMLClass umlClassTo = message.getTo();

            boolean isExist = false;
            for (UMLOperation umlOperation : umlClassTo.getOperations())
            {
                if (umlOperation.getName().equals(message.getOperation()))
                {
                    isExist = true;
                    break;
                }
            }
            if (!isExist && !message.getOperation().equals("Return"))
            {
                message.setTextStyle(1);
            }else
            {
                message.setTextStyle(0);
            }
        }
    }
}
