package uml.ija;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import uml.ija.model.ClassDiagram;
import uml.ija.model.UMLClass;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Trida pro vytvoreni tabulky
 *
 * @author Tomáš Polívka (xpoliv06)
 * @author Vojtěch Hájek (xhajek51)
 */
public class CreateTableController {

    /**
     * Funcke pro vykresleni okna pro pridani tabulky trid do diagramu
     *
     * @param classDiagram objetk diagramu trid
     */
    public static boolean display(ClassDiagram classDiagram) {
        AtomicBoolean result = new AtomicBoolean(false);
        Stage window = new Stage();
        window.setTitle("Create table");
        Label label = new Label();
        label.setText("Table name: ");
        TextField fileText = new TextField();

        Button submitButton = new Button("Submit");
        submitButton.setOnAction(event1 -> {
            String newTable = fileText.getText();
            boolean notExist = true;
            for (UMLClass umlClass : classDiagram.getClasses()) {
                if (umlClass.getName().equals(newTable)) {
                    Alert alertError = new Alert(Alert.AlertType.ERROR);
                    alertError.setContentText("Table already exist!");
                    alertError.showAndWait();
                    notExist = false;
                }
            }
            if (notExist) {
                classDiagram.createClass(newTable);
                result.set(true);
                window.close();
            }
        });

        HBox inputLayout = new HBox();
        inputLayout.getChildren().addAll(label, fileText);
        inputLayout.setAlignment(Pos.CENTER);
        inputLayout.setPadding(new Insets(20));
        inputLayout.setSpacing(20);

        VBox layout = new VBox();
        layout.getChildren().addAll(inputLayout, submitButton);
        layout.setAlignment(Pos.CENTER);
        Scene scene = new Scene(layout, 320, 150);
        window.setScene(scene);
        window.showAndWait();

        return result.get();
    }
}
