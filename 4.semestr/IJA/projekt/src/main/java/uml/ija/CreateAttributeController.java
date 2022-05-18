package uml.ija;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import uml.ija.model.ClassDiagram;
import uml.ija.model.UMLAttribute;
import uml.ija.model.UMLClass;
import uml.ija.model.UMLClassifier;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Trida pro vytvoreni atributu
 *
 * @author Tomáš Polívka (xpoliv06)
 * @author Vojtěch Hájek (xhajek51)
 */
public class CreateAttributeController {
    /**
     * Funcke pro vykresleni okna pro pridani atributu do tabulky trid
     *
     * @param classDiagram objekt tridy diagramu
     * @param umlClass     objekt tridy tabulky trid
     */
    public static boolean display(ClassDiagram classDiagram, UMLClass umlClass) {
        AtomicBoolean result = new AtomicBoolean(false);
        Stage window = new Stage();
        window.setTitle("Edit Attribute");

        Label attrLabel = new Label();
        attrLabel.setText("Attribute name:");
        attrLabel.setPadding(new Insets(5, 10, 10, 15));
        TextField attrText = new TextField();

        HBox topHBox = new HBox();
        topHBox.setPadding(new Insets(5, 15, 10, 0));
        topHBox.getChildren().addAll(attrLabel, attrText);

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

        CheckBox checkBoxIsList = new CheckBox();

        Label lb = new Label("List:");
        lb.setGraphic(checkBoxIsList);
        lb.setContentDisplay(ContentDisplay.RIGHT);
        lb.setPadding(new Insets(5, 15, 15, 15));

        vbox.getChildren().add(lb);

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

        Button submitButton = new Button("Create");
        submitButton.setOnAction(event1 -> {
            Alert alertError = new Alert(Alert.AlertType.ERROR);
            if (attrText.getText() != null && !attrText.getText().trim().isEmpty()) {
                boolean notError = true;
                for (UMLAttribute attr : umlClass.getAttributes()) {
                    if (attr.getName().equals(attrText.getText())) {
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
                    UMLAttribute attribute = new UMLAttribute(attrText.getText(), classifier, accessClassifier);
                    if (checkBoxIsList.isSelected())
                    {
                        attribute.setList(true);
                    }
                    umlClass.addAttribute(attribute);

                    result.set(true);
                    window.close();
                }
            } else {
                alertError.setContentText("Name field is empty");
                alertError.showAndWait();
            }
        });

        VBox.setMargin(submitButton, new Insets(10, 0, 0, 20));
        vbox.getChildren().add(submitButton);
        vbox.setAlignment(Pos.CENTER_LEFT);
        vbox.setPadding(new Insets(20));
        Scene scene = new Scene(vbox);
        window.setScene(scene);
        window.showAndWait();

        return result.get();
    }
}



