package uml.ija;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


/**
 * The type Create diagram controller.
 */
public class CreateDiagramController {

    /**
     * The Diagram name.
     */
    static String diagramName;

    /**
     * Funcke pro vykresleni okna pro vytvoreni diagramu
     *
     * @return the string
     */
    public static String display() {
        Stage window = new Stage();
        window.setTitle("Create Diagram");
        Label diagName = new Label();
        diagName.setText("Diagram name:");
        diagName.setPadding(new Insets(5, 10, 10, 15));
        TextField diagText = new TextField();

        HBox topHBox = new HBox();
        topHBox.setPadding(new Insets(5, 15, 10, 0));
        topHBox.getChildren().addAll(diagName, diagText);

        Button submitButton = new Button("Submit");
        submitButton.setOnAction(event1 -> {
            diagramName = diagText.getText();
            window.close();
        });

        VBox layout = new VBox();
        layout.getChildren().addAll(topHBox, submitButton);
        layout.setAlignment(Pos.CENTER);
        Scene scene = new Scene(layout, 320, 150);
        window.setScene(scene);
        window.showAndWait();

        return diagramName;
    }
}
