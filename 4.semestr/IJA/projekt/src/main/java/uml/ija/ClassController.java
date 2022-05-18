package uml.ija;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;
import jfxtras.labs.util.event.MouseControlUtil;
import uml.ija.model.*;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

/**
 * Trida pro funkce vykreslovani okna tridy diagramu
 *
 * @author Tomáš Polívka (xpoliv06)
 * @author Vojtěch Hájek (xhajek51)
 */
public class ClassController {

    /**
     * Diagram tříd.
     */
    @FXML
    public ClassDiagram classDiagram;

    /**
     * Tlacitko pro smazani
     */
    public Button deleteButton;

    /**
     * upload tlacitko
     */
    public Button uploadButton;

    /**
     * center
     */
    public AnchorPane center;

    /**
     * Zasobnik class pro operaci undo
     */
    private java.util.List<ClassDiagram> stackOfClasses = new ArrayList<>();

    /**
     * Priznak zda vykreslujeme diagram trid nebo sekvecni diagram
     */
    private boolean isClass = true;

    /**
     * Id panelu v sekvencnim diagramu
     */
    private int idTab = -1;

    /**
     * Upload.
     */
    @FXML
    /**
     * Nacte ze souboru pro vypsani diagramu trid
     */
    public void upload() {
        String file = UploadController.display();
        if (file == null) {
            return;
        }
        if (this.classDiagram == null) {
            this.addToStack(null);
        } else {
            this.addToStack(this.createNewClassToStack(this.classDiagram));
        }
        ReadWriteFile readWriteFile = new ReadWriteFile();
        this.classDiagram = readWriteFile.readFromFile(file);
        if (this.classDiagram == null)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Systém nemůže nalézt uvedený soubor nebo soubor je ve špatném formátu");
            alert.show();
        }
        if (isClass) {
            createDiagram();
        } else {
            createSequenceDiagram();
        }
    }

    /**
     * Funcke pro ulozeni diagramu
     */
    public void save() {
        if (this.classDiagram == null)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Nelze uložit prázdný diagram");
            alert.show();
            return;
        }

        String file = SaveController.display();
        if (file == null) {
            return;
        }

        ReadWriteFile readWriteFile = new ReadWriteFile();
        if (!readWriteFile.printToFile(this.classDiagram, file))
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Systém nemůže nalézt uvedenou cestu");
            alert.show();
        }
    }

    /**
     * Funkce pro vytvoreni nove tabulky
     */
    public void createTable() {
        if (this.classDiagram == null) {
            String diagramName = CreateDiagramController.display();
            if (diagramName == null) {
                return;
            }
            this.addToStack(null);
            this.classDiagram = new ClassDiagram(diagramName);
        }
        if (isClass) {
            ClassDiagram tmp = this.createNewClassToStack(this.classDiagram);
            if (CreateTableController.display(this.classDiagram)) {
                this.addToStack(tmp);
                createDiagram();
            }
        } else {
            ClassDiagram tmp = this.createNewClassToStack(this.classDiagram);
            if (CreateSequenceController.display(this.classDiagram)) {
                this.addToStack(tmp);
                createSequenceDiagram();
            }
        }
    }

    /**
     * Funkce pro smazani diagramu
     */
    public void delete() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete");
        alert.setHeaderText("Delete Diagram!");
        alert.setContentText("Are you sure you want to delete diagram?");

        Optional<ButtonType> resultAlert = alert.showAndWait();
        if (resultAlert.get() == ButtonType.OK) {
            this.addToStack(this.createNewClassToStack(this.classDiagram));
            this.classDiagram = null;
            deleteButton.setVisible(false);
            createDiagram();
        }
    }


    /**
     * Funkce pro vykresleni diagramu
     */
    public void createDiagram() {
        center.getChildren().clear();
        Stage primStage = (Stage) center.getScene().getWindow();
        if (this.classDiagram == null) {
            primStage.setTitle("UMLDiagram");
            return;
        }
        deleteButton.setVisible(true);
        primStage.setTitle(classDiagram.getName());
        this.classDiagram.checkDiagram();
        for (UMLClass umlClass : this.classDiagram.getClasses()) {
            this.addTable(umlClass);
        }
        drawLine();
    }

    /**
     * Vygenerovani tabulky trid
     *
     * @param umlClass uml trida
     */
    public void addTable(UMLClass umlClass) {
        VBox vbox = new VBox();
        vbox.computeAreaInScreen();
        vbox.getStyleClass().add("border");

        Label tableName = new Label(umlClass.getName());
        tableName.setAlignment(Pos.CENTER);
        tableName.setPadding(new Insets(5));

        tableName.setMinWidth(vbox.getWidth());
        tableName.getStyleClass().add("borderDown");
        tableName.setOnMouseClicked(event -> {
            if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) {
                ClassDiagram pom = this.createNewClassToStack(this.classDiagram);
                if (EditTableNameController.display(this.classDiagram, umlClass)) {
                    this.addToStack(pom);
                    createDiagram();
                }
            }
        });
        vbox.getChildren().add(tableName);
        double sizeString = 200;
        if (sizeString < (umlClass.getName().length() * 4)) {
            sizeString = (umlClass.getName().length() * 4);
        }

        for (UMLAttribute attr : umlClass.getAttributes()) {
            if (sizeString < (attr.toStringClass().length() * 6.5)) {
                sizeString = (attr.toStringClass().length() * 6.5);
            }
            Label attrName = new Label(attr.toStringClass());
            switch (attr.getTextStyle()) {
                case 1:
                    attrName.getStyleClass().add("redBack");
                    break;
                case 2:
                    attrName.getStyleClass().add("greenBack");
                    break;
            }
            attrName.setMinWidth(tableName.getMinWidth());
            attrName.setPadding(new Insets(1, 4, 2, 4));
            attrName.setOnMouseClicked(event -> {
                if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) {
                    ClassDiagram pom = this.createNewClassToStack(this.classDiagram);
                    if (EditAttributeController.display(umlClass, attr)) {
                        this.addToStack(pom);
                        center.getChildren().remove(vbox);
                        addTable(umlClass);
                    }
                }
            });
            vbox.getChildren().add(attrName);
        }
        Button addAttr = new Button("Create attribute");
        addAttr.setMinWidth(tableName.getMinWidth());
        addAttr.setPadding(new Insets(1, 4, 2, 4));
        addAttr.setOnMouseClicked(mouseEvent -> {
            ClassDiagram pom = this.createNewClassToStack(this.classDiagram);
            if (CreateAttributeController.display(this.classDiagram, umlClass)) {
                this.addToStack(pom);
                center.getChildren().remove(vbox);
                addTable(umlClass);
            }

        });
        vbox.getChildren().add(addAttr);
        int i = 0;
        Label a = null;
        for (UMLOperation op : umlClass.getOperations()) {
            Label opName = new Label(op.toString());
            switch (op.getTextStyle()) {
                case 1:
                    opName.getStyleClass().add("redBack");
                    break;
                case 2:
                    opName.getStyleClass().add("greenBack");
                    break;
            }
            if (sizeString < (op.toString().length() * 6.5)) {
                sizeString = (op.toString().length() * 6.5);
            }
            opName.setMinWidth(tableName.getMinWidth());
            opName.setPadding(new Insets(1, 4, 2, 4));
            if (i == 0) {
                opName.getStyleClass().add("borderTop");
                a = opName;
            }
            opName.setOnMouseClicked(event -> {
                if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) {
                    ClassDiagram pom = this.createNewClassToStack(this.classDiagram);
                    if (EditOperationController.display(umlClass, op)) {
                        this.addToStack(pom);
                        center.getChildren().remove(vbox);
                        addTable(umlClass);
                    }
                }
            });
            vbox.getChildren().add(opName);
            i += 1;
        }
        Button addOper = new Button("Create Operation");
        addOper.setMinWidth(tableName.getMinWidth());
        addOper.setPadding(new Insets(1, 4, 2, 4));
        addOper.setOnMouseClicked(mouseEvent -> {
            ClassDiagram pom = this.createNewClassToStack(this.classDiagram);
            if (CreateOperationController.display(this.classDiagram, umlClass)) {
                this.addToStack(pom);
                center.getChildren().remove(vbox);
                addTable(umlClass);
            }
        });
        vbox.getChildren().add(addOper);

        // Pokud neni nastavena pozice bere se vychozi vykresleni
        GraphicPosition position = umlClass.getPosition();
        double x = position.getX();
        double y = position.getY();

        if (x == 0.0 && y == 0.0) {
            x = 50;
            y = 30;
        }
        if (a != null) {
            a.setMinWidth(sizeString);
        }

        tableName.setMinWidth(sizeString);
        vbox.setLayoutY(y);
        vbox.setLayoutX(x);

        MouseControlUtil.makeDraggable(vbox);
        vbox.setOnMouseClicked(mouseEvent -> {
            if (vbox.getHeight() != position.getHeight()
                    || vbox.getWidth() != position.getWidth()
                    || vbox.getLayoutY() != position.getY()
                    || vbox.getLayoutX() != position.getX()) {
                this.addToStack(this.createNewClassToStack(this.classDiagram));
                position.setHeight(vbox.getHeight());
                position.setWidth(vbox.getWidth());
                position.setX(vbox.getLayoutX());
                position.setY(vbox.getLayoutY());
                createDiagram();
            }
        });
        center.getChildren().addAll(vbox);
    }

    /**
     * Funkce na ziskani pozic a vykresleni vsech vazeb
     */
    public void drawLine() {
        for (UMLRelation umlRelation : this.classDiagram.getRelations()) {
            UMLClass umlClassFrom = umlRelation.getClassFrom();
            UMLClass umlClassTo = umlRelation.getClassTo();

            if (umlClassFrom == null || umlClassTo == null) {
                continue;
            }

            GraphicPosition position1 = umlClassFrom.getPosition();
            double x1From = position1.getX();
            double x2From = position1.getX() + position1.getWidth();
            double y1From = position1.getY();
            double y2From = position1.getY() + position1.getHeight();
            double wFrom = position1.getWidth();
            double hFrom = position1.getHeight();

            GraphicPosition position2 = umlClassTo.getPosition();
            double x1To = position2.getX();
            double x2To = position2.getX() + position2.getWidth();
            double y1To = position2.getY();
            double y2To = position2.getY() + position2.getHeight();
            double wTo = position2.getWidth();
            double hTo = position2.getHeight();

//          1 |  2  | 3
//         ___|_____|____
//          4 |     | 5
//         ___|_____|____
//          6 |  7  | 8

            boolean left = false;
            boolean middle = false;
            boolean right = false;
            int quadrant = 2;

            if (x1From < x1To && x2From < x1To) {
                left = true;
            } else if (x1From > x2To && x2From > x2To) {
                right = true;
            } else {
                middle = true;
            }

            if (y1From < y1To && y2From < y1To) {
                if (left) {
                    quadrant = 6;
                }
                if (middle) {
                    quadrant = 7;
                }
                if (right) {
                    quadrant = 8;
                }
            } else if (y1From > y2To && y2From > y2To) {
                if (left) {
                    quadrant = 1;
                }
                if (right) {
                    quadrant = 3;
                }

            } else {
                if (left) {
                    quadrant = 4;
                }
                if (right) {
                    quadrant = 5;
                }
            }

            double xFrom, yFrom, xTo, yTo;
            switch (quadrant) {
                case 2:
                    xFrom = x1From + wFrom / 2;
                    yFrom = y1From;
                    xTo = x1To + wTo / 2;
                    yTo = y2To;
                    break;

                case 3:
                    xFrom = x1From;
                    yFrom = y1From;
                    xTo = x2To;
                    yTo = y2To;
                    break;

                case 4:
                    xFrom = x2From;
                    yFrom = y1From + hFrom / 2;
                    xTo = x1To;
                    yTo = y1To + hTo / 2;
                    break;

                case 5:
                    xFrom = x1From;
                    yFrom = y1From + hFrom / 2;
                    xTo = x2To;
                    yTo = y1To + hTo / 2;
                    break;

                case 6:
                    xFrom = x2From;
                    yFrom = y2From;
                    xTo = x1To;
                    yTo = y1To;
                    break;

                case 7:
                    xFrom = x1From + wFrom / 2;
                    yFrom = y2From;
                    xTo = x1To + wTo / 2;
                    yTo = y1To;
                    break;

                case 8:
                    xFrom = x1From;
                    yFrom = y2From;
                    xTo = x2To;
                    yTo = y1To;
                    break;

                default:
                    xFrom = x2From;
                    yFrom = y1From;
                    xTo = x1To;
                    yTo = y2To;
                    break;
            }

            switch (umlRelation.getTypeOfRelation().getName()) {
                // -->
                case "association":
                    drawArrowLine(xFrom, yFrom, xTo, yTo);
                    break;
                // --<>
                case "aggregation":
                    drawRhombLine(xFrom, yFrom, xTo, yTo);
                    break;
                // --<>
                case "composition":
                    drawRhombLine(xFrom, yFrom, xTo, yTo);
                    break;
                // --|>
                case "generalization":
                    drawTriangleLine(xFrom, yFrom, xTo, yTo);
                    break;
                default:
            }

        }
    }

    /**
     * Funkce pro vykresleni vazby s sipkou pro typ association
     *
     * @param centerXFrom pozice X prvni tabulky
     * @param centerYFrom pozice Y prvni tabulky
     * @param centerXTo   pozice X druhe tabulky
     * @param centerYTo   pozice Y druhe tabulky
     */
    private void drawArrowLine(double centerXFrom, double centerYFrom, double centerXTo, double centerYTo) {

        double slope = (centerYFrom - centerYTo) / (centerXFrom - centerXTo);
        double lineAngle = Math.atan(slope);

        double arrowAngle = centerXFrom > centerXTo ? Math.toRadians(45) : -Math.toRadians(225);

        Line line = new Line(centerXFrom, centerYFrom, centerXTo, centerYTo);

        double arrowLength = 15;

        // Cary pro vykresleni sipky
        Line arrow1 = new Line();
        arrow1.setStartX(line.getEndX());
        arrow1.setStartY(line.getEndY());
        arrow1.setEndX(line.getEndX() + arrowLength * Math.cos(lineAngle - arrowAngle));
        arrow1.setEndY(line.getEndY() + arrowLength * Math.sin(lineAngle - arrowAngle));

        Line arrow2 = new Line();
        arrow2.setStartX(line.getEndX());
        arrow2.setStartY(line.getEndY());
        arrow2.setEndX(line.getEndX() + arrowLength * Math.cos(lineAngle + arrowAngle));
        arrow2.setEndY(line.getEndY() + arrowLength * Math.sin(lineAngle + arrowAngle));

        center.getChildren().addAll(line, arrow1, arrow2);
    }

    /**
     * Funkce pro vykresleni vazby s trojuhleníkem pro typ generalization
     *
     * @param centerXFrom pozice X prvni tabulky
     * @param centerYFrom pozice Y prvni tabulky
     * @param centerXTo   pozice X druhe tabulky
     * @param centerYTo   pozice Y druhe tabulky
     */
    public void drawTriangleLine(double centerXFrom, double centerYFrom, double centerXTo, double centerYTo) {
        double slope = (centerYFrom - centerYTo) / (centerXFrom - centerXTo);
        double lineAngle = Math.atan(slope);

        double arrowAngle = centerXFrom > centerXTo ? Math.toRadians(45) : -Math.toRadians(225);

        double arrowLength = 15;

        Line straightLine = new Line();
        straightLine.setStartX(centerXTo + arrowLength * Math.cos(lineAngle - arrowAngle));
        straightLine.setStartY(centerYTo + arrowLength * Math.sin(lineAngle - arrowAngle));
        straightLine.setEndX(centerXTo + arrowLength * Math.cos(lineAngle + arrowAngle));
        straightLine.setEndY(centerYTo + arrowLength * Math.sin(lineAngle + arrowAngle));
        double centerX = (straightLine.getStartX() + straightLine.getEndX()) / 2;
        double centerY = (straightLine.getStartY() + straightLine.getEndY()) / 2;

        Line line = new Line(centerXFrom, centerYFrom, centerX, centerY);

        Line arrow1 = new Line();
        arrow1.setStartX(centerXTo);
        arrow1.setStartY(centerYTo);
        arrow1.setEndX(centerXTo + arrowLength * Math.cos(lineAngle - arrowAngle));
        arrow1.setEndY(centerYTo + arrowLength * Math.sin(lineAngle - arrowAngle));

        Line arrow2 = new Line();
        arrow2.setStartX(centerXTo);
        arrow2.setStartY(centerYTo);
        arrow2.setEndX(centerXTo + arrowLength * Math.cos(lineAngle + arrowAngle));
        arrow2.setEndY(centerYTo + arrowLength * Math.sin(lineAngle + arrowAngle));


        center.getChildren().addAll(line, arrow1, arrow2, straightLine);
    }

    /**
     * Funkce pro vykresleni vazby s kosoctvercem pro typy composition a aggregation
     *
     * @param centerXFrom pozice X prvni tabulky
     * @param centerYFrom pozice Y prvni tabulky
     * @param centerXTo   pozice X druhe tabulky
     * @param centerYTo   pozice Y druhe tabulky
     */
    public void drawRhombLine(double centerXFrom, double centerYFrom, double centerXTo, double centerYTo) {
        double slope = (centerYFrom - centerYTo) / (centerXFrom - centerXTo);
        double lineAngle = Math.atan(slope);

        double arrowAngle = centerXFrom > centerXTo ? Math.toRadians(45) : -Math.toRadians(225);

        double arrowLength = 12;

        Line arrow1 = new Line();
        arrow1.setStartX(centerXTo);
        arrow1.setStartY(centerYTo);
        arrow1.setEndX(centerXTo + arrowLength * Math.cos(lineAngle - arrowAngle));
        arrow1.setEndY(centerYTo + arrowLength * Math.sin(lineAngle - arrowAngle));

        Line arrow2 = new Line();
        arrow2.setStartX(centerXTo);
        arrow2.setStartY(centerYTo);
        arrow2.setEndX(centerXTo + arrowLength * Math.cos(lineAngle + arrowAngle));
        arrow2.setEndY(centerYTo + arrowLength * Math.sin(lineAngle + arrowAngle));

        Line arrow3 = new Line();
        arrow3.setStartX(arrow1.getEndX());
        arrow3.setStartY(arrow1.getEndY());
        arrow3.setEndX(arrow1.getEndX() + arrowLength * Math.cos(lineAngle + arrowAngle));
        arrow3.setEndY(arrow1.getEndY() + arrowLength * Math.sin(lineAngle + arrowAngle));

        Line arrow4 = new Line();
        arrow4.setStartX(arrow2.getEndX());
        arrow4.setStartY(arrow2.getEndY());
        arrow4.setEndX(arrow2.getEndX() + arrowLength * Math.cos(lineAngle - arrowAngle));
        arrow4.setEndY(arrow2.getEndY() + arrowLength * Math.sin(lineAngle - arrowAngle));

        Line line = new Line(centerXFrom, centerYFrom, arrow4.getEndX(), arrow4.getEndY());
        center.getChildren().addAll(line, arrow1, arrow2, arrow3, arrow4);
    }

    /**
     * Prepne na scenu pro diagram trid
     */
    public void switchToScene1() {
        this.isClass = true;
        this.stackOfClasses = new ArrayList<>();
        createDiagram();
    }

    /**
     * Prepne scenu na sekvencni diagram
     */
    public void switchToScene2() {
        center.getChildren().clear();
        this.isClass = false;
        this.stackOfClasses = new ArrayList<>();
        createSequenceDiagram();
    }


    /**
     * Klon pro nove hodnoty
     */
    private ClassDiagram createNewClassToStack(ClassDiagram classDiagram) {
        ClassDiagram cloneClassDiagram = new ClassDiagram(classDiagram.getName());

        // Pridani klasifikatoru
        for (UMLClassifier umlClassifier : classDiagram.getClassifiers()) {
            if (umlClassifier.getName().equals("string")
                    || umlClassifier.getName().equals("bool")
                    || umlClassifier.getName().equals("void")
                    || umlClassifier.getName().equals("int")) {
                continue;
            }
            UMLClassifier cloneUmlClassifier = new UMLClassifier(umlClassifier.getName(), umlClassifier.isUserDefined());
            cloneUmlClassifier.setTextStyle(umlClassifier.getTextStyle());
            cloneClassDiagram.addClassifier(cloneUmlClassifier);
        }

        for (UMLClass umlClass : classDiagram.getClasses()) {
            UMLClass cloneUmlClass = new UMLClass(umlClass.getName());

            for (UMLAttribute umlAttribute : umlClass.getAttributes()) {
                UMLAttribute cloneUmlAttribute = new UMLAttribute(umlAttribute.getName(),
                        cloneClassDiagram.findClassifier(umlAttribute.getType().getName()),
                        cloneClassDiagram.findAccessClassifier(umlAttribute.getAccessType().getName()));
                cloneUmlAttribute.setTextStyle(umlAttribute.getTextStyle());
                cloneUmlAttribute.setList(umlAttribute.isList());

                cloneUmlClass.addAttribute(cloneUmlAttribute);
            }

            for (UMLOperation umlOperation : umlClass.getOperations()) {
                UMLOperation cloneUmlOperation = new UMLOperation(umlOperation.getName(),
                        cloneClassDiagram.findClassifier(umlOperation.getType().getName()),
                        cloneClassDiagram.findAccessClassifier(umlOperation.getAccessType().getName()));
                cloneUmlOperation.setTextStyle(umlOperation.getTextStyle());

                for (UMLAttribute umlAttribute : umlOperation.getArguments()) {
                    UMLAttribute cloneUmlAttribute = new UMLAttribute(umlAttribute.getName(),
                            cloneClassDiagram.findClassifier(umlAttribute.getType().getName()));

                    cloneUmlOperation.addArgument(cloneUmlAttribute);
                }

                cloneUmlClass.addOperation(cloneUmlOperation);
            }

            GraphicPosition clonePosition = new GraphicPosition();
            clonePosition.setX(umlClass.getPosition().getX());
            clonePosition.setY(umlClass.getPosition().getY());
            clonePosition.setWidth(umlClass.getPosition().getWidth());
            clonePosition.setHeight(umlClass.getPosition().getHeight());

            cloneUmlClass.setPosition(clonePosition);
            cloneUmlClass.setExtendFrom(umlClass.getExtendFrom());

            GraphicPosition clonePositionSq = new GraphicPosition();
            clonePositionSq.setX(umlClass.getPosition().getX());
            clonePositionSq.setY(umlClass.getPosition().getY());
            clonePositionSq.setWidth(umlClass.getPosition().getWidth());
            clonePositionSq.setHeight(umlClass.getPosition().getHeight());

            cloneUmlClass.setSequencePosition(clonePositionSq);

            cloneUmlClass.setExtendFrom(umlClass.getExtendFrom());

            cloneClassDiagram.addClass(cloneUmlClass);

        }

        // Pridani relace
        for (UMLRelation umlRelation : classDiagram.getRelations()) {
            UMLRelation cloneUmlRelation = new UMLRelation(cloneClassDiagram.findClass(umlRelation.getClassFrom().getName()),
                    cloneClassDiagram.findClass(umlRelation.getClassTo().getName()),
                    cloneClassDiagram.findRelationsClassifier(umlRelation.getTypeOfRelation().getName()));
            cloneClassDiagram.addRelation(cloneUmlRelation);
        }

        // Pridani sekvencnich diagramu
        for (SequenceDiagram sequenceDiagram : classDiagram.getSequenceDiagrams()) {
            SequenceDiagram cloneSequenceDiagram = new SequenceDiagram(sequenceDiagram.getName());

            for (UMLClass umlClass : sequenceDiagram.getClasses()) {
                cloneSequenceDiagram.addClass(cloneClassDiagram.findClass(umlClass.getName()));
            }

            for (SequenceMessage sequenceMessage : sequenceDiagram.getMessages()) {
                String operation = sequenceMessage.getOperation();
                String typeOfMessage = sequenceMessage.getTypeOfMessage();
                UMLClass umlClassFrom = cloneClassDiagram.findClass(sequenceMessage.getFrom().getName());
                UMLClass umlClassTo = cloneClassDiagram.findClass(sequenceMessage.getTo().getName());
                SequenceMessage cloneSequenceMessage = new SequenceMessage(umlClassFrom, umlClassTo, typeOfMessage, operation);
                cloneSequenceMessage.setMessage(sequenceMessage.getMessage());
                cloneSequenceDiagram.addMessage(cloneSequenceMessage);
            }
            cloneClassDiagram.addSequenceDiagram(cloneSequenceDiagram);
        }

        return cloneClassDiagram;
    }

    /**
     * Pridani diagramu do zasobniku
     *
     * @param classDiagram Diagram trid
     */
    public void addToStack(ClassDiagram classDiagram) {
        if (this.stackOfClasses.size() >= 4) {
            java.util.ArrayList<ClassDiagram> tmp = new ArrayList<>();
            for (int i = 4; i > 0; i--) {
                tmp.add(this.stackOfClasses.get(this.stackOfClasses.size() - i));
            }
            this.stackOfClasses = tmp;
        }

        this.stackOfClasses.add(classDiagram);
    }

    /**
     * Vykresli sekvencni diagram
     */
    public void createSequenceDiagram() {
        center.getChildren().clear();
        TabPane tabPane = new TabPane();
        Stage primStage = (Stage) center.getScene().getWindow();
        if (this.classDiagram == null) {
            primStage.setTitle("UMLDiagram");
            return;
        }
        int id = 0;
        for (SequenceDiagram sequenceDiagram : this.classDiagram.getSequenceDiagrams()) {
            sequenceDiagram.checkSequenceDiagram();
            Tab tab = new Tab();
            tab.setText(sequenceDiagram.getName());
            tab.closableProperty();
            tab.setOnCloseRequest(event -> {
                Alert areYouSureAlert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure?", ButtonType.YES, ButtonType.NO);
                Optional<ButtonType> result = areYouSureAlert.showAndWait();
                if (result.isEmpty() || result.get() != ButtonType.YES) {
                    event.consume();
                } else {
                    this.idTab = -1;
                    this.addToStack(this.createNewClassToStack(this.classDiagram));
                    this.classDiagram.deleteSequenceDiagram(sequenceDiagram);
                    createSequenceDiagram();
                }
            });
            Button createObject = new Button("Create Object");
            createObject.setLayoutX(center.getWidth() - 150);
            createObject.setLayoutY(center.getHeight() - 100);
            int finalId = id;
            createObject.setOnMouseClicked(mouseEvent -> {
                ClassDiagram pom = this.createNewClassToStack(this.classDiagram);
                if (CreateObjSeqController.display(this.classDiagram, sequenceDiagram)) {
                    this.addToStack(pom);
                    this.idTab = finalId;
                    createSequenceDiagram();
                }
            });

            AnchorPane anchorPane = new AnchorPane();
            anchorPane.setMinWidth(center.getWidth());
            anchorPane.getChildren().add(createObject);

            int i = 0;
            for (UMLClass umlClass : sequenceDiagram.getClasses()) {
                Label classLabel = new Label(umlClass.getName());
                classLabel.setPadding(new Insets(10, 20, 10, 20));
                classLabel.getStyleClass().add("border2");
                classLabel.setLayoutX(i * 250 + 50);
                classLabel.setLayoutY(50);
                classLabel.setMinWidth(umlClass.getName().length() * 7.5);
                classLabel.setMinHeight(50);

                GraphicPosition position = new GraphicPosition();
                position.setHeight(50);
                position.setWidth(umlClass.getName().length() * 7.5);
                position.setX(i * 250 + 50);
                position.setY(50);
                umlClass.setSequencePosition(position);

                classLabel.setOnMouseClicked(event -> {
                    ClassDiagram pom = this.createNewClassToStack(this.classDiagram);
                    if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) {
                        if (CreateSqMessController.display(sequenceDiagram, umlClass)) {
                            this.addToStack(pom);
                            this.idTab = finalId;
                            createSequenceDiagram();
                        }
                    }
                });
                anchorPane.getChildren().add(classLabel);
                i++;
            }

            int countOfMessages = sequenceDiagram.getMessages().size();
            // Vykresleni cary pod diagramem
            for (UMLClass umlClass : sequenceDiagram.getClasses()) {
                if (umlClass.getSequencePosition() == null) {
                    continue;
                }
                Line line = new Line();
                double width = umlClass.getSequencePosition().getX() + (umlClass.getSequencePosition().getWidth() / 2);
                double height = umlClass.getSequencePosition().getY() + umlClass.getSequencePosition().getHeight();
                line.setStartX(width);
                line.setStartY(height);

                line.setEndX(width);
                line.setEndY(height + countOfMessages * 60);
                line.getStrokeDashArray().addAll(10d, 5d);
                anchorPane.getChildren().add(line);
            }

            int j = 1;
            for (SequenceMessage message : sequenceDiagram.getMessages()) {
                if (message.getFrom().getSequencePosition() == null || message.getTo().getSequencePosition() == null) {
                    continue;
                }

                double centerFromX = message.getFrom().getSequencePosition().getX() + (message.getFrom().getSequencePosition().getWidth() / 2);
                double centerToX = message.getTo().getSequencePosition().getX() + (message.getTo().getSequencePosition().getWidth() / 2);

                if (message.getMessage() != null && !Objects.equals(message.getMessage(), "") && !Objects.equals(message.getMessage(), "Return")) {
                    Label messageLabel = new Label(message.getMessage());
                    if (centerFromX < centerToX) {
                        messageLabel.setLayoutX(centerFromX + 50);
                        messageLabel.setLayoutY(60 * j + 50);
                    } else {
                        messageLabel.setLayoutX(centerToX + 50);
                        messageLabel.setLayoutY(60 * j + 50);
                    }
                    if (message.getTextStyle() == 1) {
                        messageLabel.getStyleClass().add("redBack");
                    }

                    anchorPane.getChildren().add(messageLabel);
                }
                Line fullLine;
                if (centerFromX == centerToX) {
                    fullLine = new Line(centerFromX, 60 * j + 80, centerFromX + 100, 60 * j + 80);
                    Line fullLine2 = new Line(centerFromX + 100, 60 * j + 80, centerFromX + 100, 60 * j + 110);
                    Line fullLine3 = new Line(centerFromX + 100, 60 * j + 110, centerFromX, 60 * j + 110);
                    if (message.getTypeOfMessage().equals("Dashed")) {
                        fullLine.getStrokeDashArray().addAll(10d, 5d);
                        fullLine2.getStrokeDashArray().addAll(10d, 5d);
                        fullLine3.getStrokeDashArray().addAll(10d, 5d);
                    }
                    Point2D point1 = new Point2D(fullLine3.getEndX(), fullLine3.getEndY());
                    Point2D point2 = new Point2D(fullLine3.getEndX() + 10, fullLine3.getEndY() + 10);
                    Point2D point3 = new Point2D(fullLine3.getEndX() + 10, fullLine3.getEndY() - 10);
                    Polygon polygon = createTriangle(point1, point2, point3);

                    if (message.getTextStyle() == 1) {
                        fullLine.setStroke(Color.RED);
                        fullLine2.setStroke(Color.RED);
                        fullLine3.setStroke(Color.RED);
                        polygon.setStroke(Color.RED);
                    }

                    anchorPane.getChildren().addAll(fullLine, fullLine2, fullLine3, polygon);
                    j++;
                    continue;
                }

                if (message.getTypeOfMessage().equals("Full")) {
                    fullLine = new Line(centerFromX, 60 * j + 80, centerToX, 60 * j + 80);
                    if (message.getTextStyle() == 1) {
                        fullLine.setStroke(Color.RED);
                    }

                    anchorPane.getChildren().addAll(fullLine);
                } else {
                    fullLine = new Line(centerFromX, 60 * j + 70, centerToX, 60 * j + 70);
                    fullLine.getStrokeDashArray().addAll(10d, 5d);
                    if (message.getTextStyle() == 1) {
                        fullLine.setStroke(Color.RED);
                    }
                    anchorPane.getChildren().add(fullLine);
                }

                if (centerFromX < centerToX) {
                    Point2D point1 = new Point2D(fullLine.getEndX(), fullLine.getEndY());
                    Point2D point2 = new Point2D(fullLine.getEndX() - 10, fullLine.getEndY() + 10);
                    Point2D point3 = new Point2D(fullLine.getEndX() - 10, fullLine.getEndY() - 10);
                    Polygon polygon = createTriangle(point1, point2, point3);
                    if (message.getTextStyle() == 1) {
                        polygon.setFill(Color.RED);
                    }
                    anchorPane.getChildren().add(polygon);
                } else {
                    Point2D point1 = new Point2D(fullLine.getEndX(), fullLine.getEndY());
                    Point2D point2 = new Point2D(fullLine.getEndX() + 10, fullLine.getEndY() + 10);
                    Point2D point3 = new Point2D(fullLine.getEndX() + 10, fullLine.getEndY() - 10);
                    Polygon polygon = createTriangle(point1, point2, point3);
                    if (message.getTextStyle() == 1) {
                        polygon.setFill(Color.RED);
                    }
                    anchorPane.getChildren().add(polygon);
                }
                j++;
            }
            tab.setContent(anchorPane);
            tabPane.getTabs().add(tab);
            id++;
        }
        if (this.idTab != -1) {
            tabPane.getSelectionModel().select(this.idTab);
        }
        center.getChildren().add(tabPane);
    }

    /**
     * Vytvori trojuhelnik.
     *
     * @param p1 bod 1
     * @param p2 bod 2
     * @param p3 bod 3
     * @return trojuhlenik
     */
    Polygon createTriangle(Point2D p1, Point2D p2, Point2D p3) {
        Point2D centre = p1.midpoint(p2).midpoint(p3);
        Point2D p1Corrected = p1.subtract(centre);
        Point2D p2Corrected = p2.subtract(centre);
        Point2D p3Corrected = p3.subtract(centre);
        Polygon polygon = new Polygon(
                p1Corrected.getX(), p1Corrected.getY(),
                p2Corrected.getX(), p2Corrected.getY(),
                p3Corrected.getX(), p3Corrected.getY()
        );
        polygon.setLayoutX(centre.getX());
        polygon.setLayoutY(centre.getY());
        return polygon;
    }

    /**
     * Funkce na vraceni operace
     */
    public void undo() {
        if (this.stackOfClasses.size() == 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Nelze provést operaci undo");
            alert.show();
        } else {
            this.classDiagram = this.stackOfClasses.get(this.stackOfClasses.size() - 1);
            this.stackOfClasses.remove(this.stackOfClasses.size() - 1);
            if (this.isClass) {
                createDiagram();
            } else {
                this.idTab = -1;
                createSequenceDiagram();
            }
        }
    }
}