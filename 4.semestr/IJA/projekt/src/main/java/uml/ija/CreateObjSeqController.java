package uml.ija;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import uml.ija.model.ClassDiagram;
import uml.ija.model.SequenceDiagram;
import uml.ija.model.UMLClass;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Trida pro vytvoreni obejktu v sekvencim diagramu
 *
 * @author Tomáš Polívka (xpoliv06)
 * @author Vojtěch Hájek (xhajek51)
 */
public class CreateObjSeqController {
    /**
     * Funcke pro vykresleni okna pro vytvoření objektu v sekvenčním diagramu
     *
     * @param classDiagram    Diagram trid
     * @param sequenceDiagram Objekt sekvenčního diagramu
     * @return boolean
     */
    public static boolean display(ClassDiagram classDiagram, SequenceDiagram sequenceDiagram) {
        AtomicBoolean result = new AtomicBoolean(false);
        Stage window = new Stage();
        window.setTitle("Create object");

        Label seqLabel = new Label();
        seqLabel.setText("Object name:");
        seqLabel.setPadding(new Insets(5, 10, 10, 20));


        ChoiceBox choiceBoxClass = new ChoiceBox();
        for (UMLClass umlClass : classDiagram.getClasses()) {
            boolean isExist = false;
            for (UMLClass umlClass1 : sequenceDiagram.getClasses()) {
                if (umlClass == umlClass1) {
                    isExist = true;
                    break;
                }
            }
            if (!isExist) {
                choiceBoxClass.getItems().add(umlClass.getName());
            }
        }

        HBox topHBox = new HBox();
        topHBox.setPadding(new Insets(5, 0, 10, 0));
        topHBox.getChildren().addAll(seqLabel, choiceBoxClass);
        VBox vbox = new VBox();
        vbox.computeAreaInScreen();
        vbox.setMinWidth(150);
        vbox.getChildren().add(topHBox);

        Button submitButton = new Button("Create");
        submitButton.setOnAction(event1 -> {
            if (choiceBoxClass.getSelectionModel().getSelectedItem() != null) {
                UMLClass umlClass = classDiagram.findClass(choiceBoxClass.getSelectionModel().getSelectedItem().toString());
                sequenceDiagram.addClass(umlClass);
                result.set(true);
                window.close();
            } else {
                Alert alertError = new Alert(Alert.AlertType.ERROR);
                alertError.setContentText("You have to choose object!");
                alertError.showAndWait();
            }
        });

        vbox.getChildren().add(submitButton);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(20));
        Scene scene = new Scene(vbox);
        window.setScene(scene);
        window.showAndWait();

        return result.get();
    }
}
