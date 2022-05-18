package uml.ija;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import uml.ija.model.SequenceDiagram;
import uml.ija.model.SequenceMessage;
import uml.ija.model.UMLClass;
import uml.ija.model.UMLOperation;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Trida pro vytvoreni zpravy
 *
 * @author Tomáš Polívka (xpoliv06)
 * @author Vojtěch Hájek (xhajek51)
 */
public class CreateSqMessController {

    /**
     * Funcke pro vykresleni okna pro pridani zpravy do sekvencniho diagramu
     *
     * @param sequenceDiagram Sekvencni diagram
     * @param umlClass        Uml trida
     * @return boolean
     */
    public static boolean display(SequenceDiagram sequenceDiagram, UMLClass umlClass) {
        AtomicBoolean result = new AtomicBoolean(false);
        Stage window = new Stage();
        window.setTitle("Create message");

        Label seqLabel = new Label();
        seqLabel.setText("Choose function:");
        seqLabel.setPadding(new Insets(5, 10, 10, 20));


        ChoiceBox choiceBoxOp = new ChoiceBox();
        for (UMLOperation umlOperation : umlClass.getOperations()) {
            choiceBoxOp.getItems().add(umlOperation.getName());
        }
        choiceBoxOp.getItems().add("Return");

        HBox selectBox = new HBox();
        selectBox.getChildren().addAll(seqLabel, choiceBoxOp);
        VBox vBox = new VBox();
        vBox.getChildren().add(selectBox);

        Label seqLabelTo = new Label();
        seqLabelTo.setText("Choose object:");
        seqLabelTo.setPadding(new Insets(5, 10, 10, 20));


        ChoiceBox choiceBoxClass = new ChoiceBox();
        for (UMLClass umlClass1 : sequenceDiagram.getClasses()) {
            choiceBoxClass.getItems().add(umlClass1.getName());
        }

        HBox selectClassBox = new HBox();
        selectClassBox.getChildren().addAll(seqLabelTo, choiceBoxClass);


        vBox.getChildren().add(selectClassBox);

        final ToggleGroup typeLine = new ToggleGroup();
        RadioButton rb1 = new RadioButton();
        rb1.setText("Full");
        rb1.setPadding(new Insets(5, 20, 10, 30));
        rb1.setToggleGroup(typeLine);
        rb1.setUserData("Full");

        RadioButton rb2 = new RadioButton();
        rb2.setText("Dashed");
        rb2.setPadding(new Insets(5, 20, 10, 30));
        rb2.setToggleGroup(typeLine);
        rb2.setUserData("Dashed");

        HBox buttonBox = new HBox();
        buttonBox.getChildren().addAll(rb1, rb2);

        vBox.getChildren().add(buttonBox);
        Button createButton = new Button("Create");
        createButton.setOnMouseClicked(event -> {
            Alert alertError = new Alert(Alert.AlertType.ERROR);
            if (choiceBoxClass.getSelectionModel().getSelectedItem() != null && typeLine.getSelectedToggle() != null && choiceBoxOp.getSelectionModel().getSelectedItem() != null) {
                if (!choiceBoxOp.getSelectionModel().getSelectedItem().toString().equals("Return")) {
                    UMLClass umlClass1 = sequenceDiagram.findClass(choiceBoxClass.getSelectionModel().getSelectedItem().toString());
                    UMLOperation operation = umlClass.getOperation(choiceBoxOp.getSelectionModel().getSelectedItem().toString());
                    SequenceMessage message = new SequenceMessage(umlClass1, umlClass, typeLine.getSelectedToggle().getUserData().toString(), operation.getName());
                    if (operation.getArguments().isEmpty()) {
                        String operText = operation.toString();
                        operText = operText.substring(2);
                        message.setMessage(operText);
                        sequenceDiagram.addMessage(message);
                        result.set(true);
                        window.close();
                    } else if (CreateSqParController.display(operation, message)) {
                        sequenceDiagram.addMessage(message);
                        result.set(true);
                        window.close();
                    } else {
                        alertError.setContentText("You have to fill arguments!");
                        alertError.showAndWait();
                    }
                } else {
                    if (typeLine.getSelectedToggle().getUserData().toString().equals("Dashed")) {
                        UMLClass umlClass1 = sequenceDiagram.findClass(choiceBoxClass.getSelectionModel().getSelectedItem().toString());
                        SequenceMessage message = new SequenceMessage(umlClass1, umlClass, typeLine.getSelectedToggle().getUserData().toString(), "Return");
                        sequenceDiagram.addMessage(message);
                        result.set(true);
                        window.close();
                    } else {
                        alertError.setContentText("You canť use return function with Full line!");
                        alertError.showAndWait();
                    }
                }
            } else {
                alertError.setContentText("You have to choose operation, object and type!");
                alertError.showAndWait();
            }
        });
        Button delete = new Button("Delete object");
        delete.setOnMouseClicked(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete");
            alert.setHeaderText("Delete object!");
            alert.setContentText("Are you sure you want to delete this object?");

            Optional<ButtonType> resultAlert = alert.showAndWait();
            if (resultAlert.get() == ButtonType.OK) {
                sequenceDiagram.deleteClass(umlClass);
                result.set(true);
                window.close();
            }
        });

        vBox.getChildren().add(createButton);
        vBox.setPadding(new Insets(20));
        Scene scene = new Scene(vBox);
        window.setScene(scene);
        window.showAndWait();


        return result.get();
    }
}
