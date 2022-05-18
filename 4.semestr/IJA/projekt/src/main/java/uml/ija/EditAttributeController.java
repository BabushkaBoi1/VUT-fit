package uml.ija;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import uml.ija.model.UMLAttribute;
import uml.ija.model.UMLClass;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Trida pro editaci atributu
 *
 * @author Tomáš Polívka (xpoliv06)
 * @author Vojtěch Hájek (xhajek51)
 */
public class EditAttributeController {
    /**
     * Funcke pro vykresleni okna pro editaci atributu tabulky trid
     *
     * @param umlClass objekt tabulky trid
     * @param attr     objekt konkretniho atributu
     */
    public static boolean display(UMLClass umlClass, UMLAttribute attr) {
        AtomicBoolean result = new AtomicBoolean(false);
        Stage window = new Stage();
        window.setTitle("Edit Attribute");

        Label attrLabel = new Label();
        attrLabel.setText("Attribute name:");
        attrLabel.setPadding(new Insets(5, 10, 10, 20));
        TextField attrText = new TextField();
        attrText.setText(attr.getName());

        HBox topHBox = new HBox();
        topHBox.setPadding(new Insets(5, 0, 10, 0));
        topHBox.getChildren().addAll(attrLabel, attrText);
        VBox vbox = new VBox();
        vbox.computeAreaInScreen();
        vbox.setMinWidth(150);
        vbox.getChildren().add(topHBox);

        Button deleteButton = new Button("Delete");
        deleteButton.setOnAction(actionEvent -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete");
            alert.setHeaderText("Delete attribute!");
            alert.setContentText("Are you sure you want to delete this attribute?");

            Optional<ButtonType> resultAlert = alert.showAndWait();
            if (resultAlert.get() == ButtonType.OK) {
                umlClass.removeAttr(attr);
                result.set(true);
                window.close();
            }
        });
        Button submitButton = new Button("Rename");
        submitButton.setOnAction(event1 -> {
            if (attrText.getText() != null && !attrText.getText().trim().isEmpty()) {
                attr.rename(attrText.getText());
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
