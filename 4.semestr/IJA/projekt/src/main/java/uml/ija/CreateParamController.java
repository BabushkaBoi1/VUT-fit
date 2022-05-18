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
import uml.ija.model.UMLClassifier;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Trida pro vytvoreni parametru
 *
 * @author Tomáš Polívka (xpoliv06)
 * @author Vojtěch Hájek (xhajek51)
 */
public class CreateParamController {
    /**
     * Funcke pro vykresleni okna pro pridani parametru do operace
     *
     * @param classDiagram objekt tridy diagramu
     * @param attributes   pole parametru
     */
    public static boolean display(ClassDiagram classDiagram, java.util.List<UMLAttribute> attributes) {
        Stage window = new Stage();
        window.setTitle("Edit Attribute");
        AtomicBoolean result = new AtomicBoolean(false);
        Label attrLabel = new Label();
        attrLabel.setText("Attribute name:");
        attrLabel.setPadding(new Insets(5, 10, 10, 15));
        TextField attrText = new TextField();

        HBox topHBox = new HBox();
        topHBox.setPadding(new Insets(5, 15, 10, 0));
        topHBox.getChildren().addAll(attrLabel, attrText);

        VBox vbox = new VBox();
        vbox.getChildren().add(topHBox);
        vbox.computeAreaInScreen();
        vbox.setMinWidth(150);


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
                if (classif.getSelectedToggle() == null) {
                    alertError.setContentText("You have to select Accessibility modifier and Classifier");
                    alertError.showAndWait();
                } else {
                    boolean exist = true;
                    for (UMLAttribute attr : attributes) {
                        if (Objects.equals(attr.getName(), attrText.getText())) {
                            exist = false;
                        }
                    }
                    if (exist) {
                        UMLClassifier classifier = classDiagram.findClassifier(classif.getSelectedToggle().getUserData().toString());
                        UMLAttribute attribute = new UMLAttribute(attrText.getText(), classifier);
                        attributes.add(attribute);
                        result.set(true);
                        window.close();
                    } else {
                        alertError.setContentText("Parameter already exists");
                        alertError.showAndWait();
                    }

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
