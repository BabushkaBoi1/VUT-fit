package uml.ija;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import uml.ija.model.ClassDiagram;
import uml.ija.model.UMLClass;
import uml.ija.model.UMLClassifier;
import uml.ija.model.UMLRelation;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Trida pro editaci tabulky trid
 *
 * @author Tomáš Polívka (xpoliv06)
 * @author Vojtěch Hájek (xhajek51)
 */
public class EditTableNameController {
    /**
     * Funkce pro vykresleni okna pro editaci tabulky trid (jmena, vazeb)
     *
     * @param classDiagram objekt diagramu trid
     * @param umlClass     objekt tabulky trid
     */
    public static boolean display(ClassDiagram classDiagram, UMLClass umlClass) {
        AtomicBoolean result = new AtomicBoolean(false);
        Stage window = new Stage();
        window.setTitle("Edit table");

        Label nameTable = new Label();
        nameTable.setText("Table name:");
        nameTable.setPadding(new Insets(5, 10, 10, 20));
        TextField nameTableText = new TextField();
        nameTableText.setText(umlClass.getName());

        HBox topHBox = new HBox();
        topHBox.setPadding(new Insets(5, 0, 10, 0));
        topHBox.getChildren().addAll(nameTable, nameTableText);
        VBox vbox = new VBox();
        vbox.computeAreaInScreen();
        vbox.setMinWidth(150);
        vbox.getChildren().add(topHBox);

        Label relationLabel = new Label("Relations:");
        relationLabel.setPadding(new Insets(5, 10, 5, 20));
        vbox.getChildren().add(relationLabel);

        for (UMLRelation relation : classDiagram.getRelations()) {
            if (relation.getClassFrom() != umlClass) {
                continue;
            }
            Label classrLabel = new Label(relation.getClassTo().getName() + ": " + relation.getTypeOfRelation().getName());
            classrLabel.setPadding(new Insets(5, 10, 5, 30));
            Button deleteRelation = new Button("X");
            deleteRelation.setOnMouseClicked(mouseEvent -> {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Delete");
                alert.setHeaderText("Delete relation!");
                alert.setContentText("Are you sure you want to delete this relation?");

                Optional<ButtonType> resultAlert = alert.showAndWait();
                if (resultAlert.get() == ButtonType.OK) {
                    classDiagram.deleteRelation(relation);
                    result.set(true);
                    window.close();
                }
            });
            HBox relationHBox = new HBox();
            relationHBox.getChildren().addAll(classrLabel, deleteRelation);
            vbox.getChildren().add(relationHBox);
        }

        Label addRelationLabel = new Label("Add relation:");
        addRelationLabel.setPadding(new Insets(5, 10, 5, 20));
        vbox.getChildren().add(addRelationLabel);

        ChoiceBox choiceBoxClass = new ChoiceBox();
        choiceBoxClass.getItems().add(null);
        for (UMLClass umlClass1 : classDiagram.getClasses()) {
            if (umlClass != umlClass1) {
                choiceBoxClass.getItems().add(umlClass1.getName());
            }
        }

        ChoiceBox choiceBoxRelation = new ChoiceBox();
        choiceBoxRelation.getItems().add(null);
        for (UMLClassifier classifier : classDiagram.getRelationsClassifiers()) {
            choiceBoxRelation.getItems().add(classifier.getName());
        }

        HBox middlehBox = new HBox(choiceBoxClass, choiceBoxRelation);
        middlehBox.setSpacing(20);
        middlehBox.setPadding(new Insets(5, 0, 10, 20));
        vbox.getChildren().add(middlehBox);

        Button deleteButton = new Button("Delete table");
        deleteButton.setOnAction(event1 -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete");
            alert.setHeaderText("Delete table!");
            alert.setContentText("Are you sure you want to delete this table?");

            Optional<ButtonType> resultAlert = alert.showAndWait();
            if (resultAlert.get() == ButtonType.OK) {
                classDiagram.deleteClass(umlClass.getName());
                result.set(true);
                window.close();
            }
        });

        Button submitButton = new Button("Save");
        submitButton.setOnAction(event1 -> {
            Alert alertError = new Alert(Alert.AlertType.ERROR);
            if (nameTableText.getText() != null && !nameTableText.getText().trim().isEmpty()) {

                // Kontrola jestli zadana tabulka exituje
                UMLClass classToRename = classDiagram.findClass(nameTableText.getText());

                if (classToRename != null && classToRename != umlClass) {
                    alertError.setContentText("The table already exists");
                    alertError.showAndWait();
                } else {
                    if (choiceBoxClass.getSelectionModel().getSelectedItem() != null && choiceBoxRelation.getSelectionModel().getSelectedItem() == null) {
                        alertError.setContentText("You can't add relation without relation type");
                        alertError.showAndWait();
                    } else if (choiceBoxClass.getSelectionModel().getSelectedItem() == null && choiceBoxRelation.getSelectionModel().getSelectedItem() != null) {
                        alertError.setContentText("You can't add relation without relation type");
                        alertError.showAndWait();
                    } else if (choiceBoxClass.getSelectionModel().getSelectedItem() == null && choiceBoxRelation.getSelectionModel().getSelectedItem() == null) {
                        classDiagram.renameClassInDiagram(umlClass.getName(), nameTableText.getText());
                        result.set(true);
                        window.close();
                    } else {
                        boolean notExist = true;
                        for (UMLRelation relation : classDiagram.getRelations()) {
                            if ((Objects.equals(relation.getClassFrom().getName(), choiceBoxClass.getSelectionModel().getSelectedItem().toString()) &&
                                Objects.equals(relation.getClassTo().getName(), umlClass.getName())) ||
                                (Objects.equals(relation.getClassTo().getName(), choiceBoxClass.getSelectionModel().getSelectedItem().toString()) &&
                                Objects.equals(relation.getClassFrom().getName(), umlClass.getName()))) {
                                alertError.setContentText("Relation between these tables already exist!");
                                alertError.showAndWait();
                                notExist = false;
                            }
                        }
                        if (notExist) {
                            UMLClassifier classifier = new UMLClassifier(choiceBoxRelation.getSelectionModel().getSelectedItem().toString());
                            UMLRelation relation = new UMLRelation();
                            relation.setTypeOfRelation(classifier);
                            relation.setClassFrom(umlClass);
                            UMLClass umlClassTo = classDiagram.findClass(choiceBoxClass.getSelectionModel().getSelectedItem().toString());
                            relation.setClassTo(umlClassTo);

                            classDiagram.addRelation(relation);
                            classDiagram.renameClassInDiagram(umlClass.getName(), nameTableText.getText());
                            result.set(true);
                            window.close();
                        }
                    }
                }
            } else {
                alertError.setContentText("Name field is empty");
                alertError.showAndWait();
            }
        });
        HBox bottomBox = new HBox();
        bottomBox.getChildren().addAll(deleteButton, submitButton);
        bottomBox.setAlignment(Pos.CENTER_RIGHT);
        bottomBox.setSpacing(20);
        vbox.getChildren().add(bottomBox);
        vbox.setPadding(new Insets(20));
        Scene scene = new Scene(vbox);
        window.setScene(scene);
        window.showAndWait();

        return result.get();
    }
}
