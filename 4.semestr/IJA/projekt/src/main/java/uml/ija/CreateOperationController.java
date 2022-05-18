package uml.ija;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import uml.ija.model.*;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Trida pro vytvoreni operace
 *
 * @author Tomáš Polívka (xpoliv06)
 * @author Vojtěch Hájek (xhajek51)
 */
public class CreateOperationController {
    /**
     * Funcke pro vykresleni okna pro pridani operace do tabulky trid
     *
     * @param classDiagram objekt tridy diagramu
     * @param umlClass     objekt tridy tabulky trid
     */
    public static boolean display(ClassDiagram classDiagram, UMLClass umlClass) {
        AtomicBoolean result = new AtomicBoolean(false);
        Stage window = new Stage();
        window.setTitle("Edit Operation");

        Label operLabel = new Label();
        operLabel.setText("Operation name:");
        operLabel.setPadding(new Insets(5, 10, 10, 20));
        TextField operText = new TextField();

        HBox topHBox = new HBox();
        topHBox.setPadding(new Insets(5, 0, 10, 0));
        topHBox.getChildren().addAll(operLabel, operText);
        VBox vbox = new VBox();
        vbox.computeAreaInScreen();
        vbox.setMinWidth(150);

        Label modifLabel = new Label();
        modifLabel.setText("Accessibility modifier:");
        modifLabel.setPadding(new Insets(5, 15, 15, 15));
        vbox.getChildren().addAll(topHBox, modifLabel);

        final ToggleGroup modifier = new ToggleGroup();
        for (UMLClassifier classifier : classDiagram.getAccessClassifiers()) {
            RadioButton rb = new RadioButton(classifier.getName());
            rb.setPadding(new Insets(5, 20, 10, 30));
            rb.setToggleGroup(modifier);
            rb.setUserData(classifier.getName());
            vbox.getChildren().add(rb);
        }

        Label classifierLabel = new Label();
        classifierLabel.setText("Classifier:");
        classifierLabel.setPadding(new Insets(5, 15, 15, 15));
        vbox.getChildren().add(classifierLabel);
        final ToggleGroup classif = new ToggleGroup();
        for (UMLClassifier classifier : classDiagram.getClassifiers()) {
            RadioButton rb = new RadioButton(classifier.getName());
            rb.setPadding(new Insets(5, 20, 10, 30));
            rb.setToggleGroup(classif);
            rb.setUserData(classifier.getName());
            vbox.getChildren().add(rb);
        }

        java.util.List<UMLAttribute> attributes = new ArrayList<>();
        Button addParam = new Button("Add Parameter");
        addParam.setOnMouseClicked(mouseEvent -> {
            boolean same = CreateParamController.display(classDiagram, attributes);
            if (same) {
                UMLAttribute attr = attributes.get(attributes.size() - 1);
                Label attrName = new Label(attr.toStringClass());
                attrName.setPadding(new Insets(5, 0, 5, 35));
                double height = vbox.getHeight() + attributes.size() * 30;
                vbox.getChildren().add(attrName);
                window.setHeight(height);
            }

        });


        Button submitButton = new Button("Create");
        submitButton.setOnAction(event1 -> {
            Alert alertError = new Alert(Alert.AlertType.ERROR);
            if (operText.getText() != null && !operText.getText().trim().isEmpty()) {
                boolean notError = true;
                for (UMLOperation oper : umlClass.getOperations()) {
                    if (oper.getName().equals(operText.getText())) {
                        alertError.setContentText("Attribute already exist in table");
                        alertError.showAndWait();
                        notError = false;
                    }
                }
                if (classif.getSelectedToggle() == null || modifier.getSelectedToggle() == null) {
                    alertError.setContentText("You have to select Accessibility modifier and Classifier");
                    alertError.showAndWait();
                    notError = false;
                }
                if (notError) {
                    UMLClassifier accessClassifier = classDiagram.findAccessClassifier(modifier.getSelectedToggle().getUserData().toString());
                    UMLClassifier classifier = classDiagram.findClassifier(classif.getSelectedToggle().getUserData().toString());
                    UMLOperation operation = UMLOperation.create(operText.getText(), classifier, accessClassifier, attributes);
                    umlClass.addOperation(operation);
                    result.set(true);

                    window.close();
                }
            } else {
                alertError.setContentText("Name field is empty");
                alertError.showAndWait();
            }
        });
        HBox buttonBox = new HBox();
        buttonBox.setSpacing(30);
        buttonBox.getChildren().addAll(addParam, submitButton);
        vbox.getChildren().add(buttonBox);
        Label par = new Label("Parameters:");
        par.setPadding(new Insets(5, 0, 0, 5));
        vbox.getChildren().add(par);
        vbox.setAlignment(Pos.CENTER_LEFT);
        vbox.setPadding(new Insets(20));
        Scene scene = new Scene(vbox);
        window.setScene(scene);
        window.showAndWait();

        return result.get();
    }
}
