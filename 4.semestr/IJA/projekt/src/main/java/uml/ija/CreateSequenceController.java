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
import uml.ija.model.SequenceDiagram;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Trida pro vytvoreni tabulky
 *
 * @author Tomáš Polívka (xpoliv06)
 * @author Vojtěch Hájek (xhajek51)
 */
public class CreateSequenceController {

    /**
     * Funcke pro vykresleni okna pro pridani tabulky trid do diagramu
     *
     * @param classDiagram objetk diagramu trid
     */
    public static boolean display(ClassDiagram classDiagram) {
        AtomicBoolean result = new AtomicBoolean(false);
        Stage window = new Stage();
        window.setTitle("Create sequence diagram");

        Label label = new Label();
        label.setText("Diagram name: ");
        label.setPadding(new Insets(5, 10, 10, 20));
        TextField fileText = new TextField();

        Button submitButton = new Button("Submit");
        submitButton.setOnAction(event1 -> {
            String newTable = fileText.getText();
            boolean notExist = true;
            for (SequenceDiagram diagram : classDiagram.getSequenceDiagrams()) {
                if (diagram.getName().equals(newTable)) {
                    Alert alertError = new Alert(Alert.AlertType.ERROR);
                    alertError.setContentText("Sequence diagram already exist!");
                    alertError.showAndWait();
                    notExist = false;
                }
            }
            if (notExist) {
                classDiagram.createSequenceDiagram(newTable);
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
        layout.setPadding(new Insets(20));
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();

        return result.get();
    }
}
