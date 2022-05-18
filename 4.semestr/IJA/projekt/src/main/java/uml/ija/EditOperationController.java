package uml.ija;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import uml.ija.model.UMLClass;
import uml.ija.model.UMLOperation;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Trida pro editaci operace
 *
 * @author Tomáš Polívka (xpoliv06)
 * @author Vojtěch Hájek (xhajek51)
 */
public class EditOperationController {
    /**
     * Funcke pro vykresleni okna pro editaci operace tabulky trid
     *
     * @param umlClass  objekt tabulky trid
     * @param operation objekt konkretni operace
     */
    public static boolean display(UMLClass umlClass, UMLOperation operation) {
        AtomicBoolean result = new AtomicBoolean(false);
        Stage window = new Stage();
        window.setTitle("Edit Operation");

        Label operLabel = new Label();
        operLabel.setText("Operation name:");
        operLabel.setPadding(new Insets(5, 10, 10, 20));
        TextField operText = new TextField();
        operText.setText(operation.getName());

        HBox topHBox = new HBox();
        topHBox.setPadding(new Insets(5, 0, 10, 0));
        topHBox.getChildren().addAll(operLabel, operText);
        VBox vbox = new VBox();
        vbox.computeAreaInScreen();
        vbox.setMinWidth(150);
        vbox.getChildren().add(topHBox);

        Button deleteButton = new Button("Delete");
        deleteButton.setOnAction(actionEvent -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete");
            alert.setHeaderText("Delete operation from table!");
            alert.setContentText("Are you sure you want to delete this operation?");

            Optional<ButtonType> resultAlert = alert.showAndWait();
            if (resultAlert.get() == ButtonType.OK) {
                umlClass.removeOperation(operation);
                result.set(true);
                window.close();
            }
        });
        Button submitButton = new Button("Rename");
        submitButton.setOnAction(event1 -> {
            if (operText.getText() != null && !operText.getText().trim().isEmpty()) {
                operation.rename(operText.getText());
                result.set(true);
                window.close();
            } else {
                Alert alertError = new Alert(Alert.AlertType.ERROR);
                alertError.setContentText("Name field is empty");
                alertError.showAndWait();
            }
        });

        HBox bottomBox = new HBox();
        bottomBox.setAlignment(Pos.CENTER_RIGHT);
        bottomBox.setSpacing(20);
        bottomBox.getChildren().addAll(deleteButton, submitButton);
        vbox.getChildren().add(bottomBox);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(20));
        Scene scene = new Scene(vbox);
        window.setScene(scene);
        window.showAndWait();

        return result.get();
    }
}
