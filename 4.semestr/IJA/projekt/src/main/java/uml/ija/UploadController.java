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
 * Trida pro nacteni diagramu
 *
 * @author Tomáš Polívka (xpoliv06)
 * @author Vojtěch Hájek (xhajek51)
 */
public class UploadController {
    /**
     * The File name.
     */
    static String fileName;

    /**
     * Display string.
     *
     * @return the string
     */
    public static String display() {
        Stage window = new Stage();
        window.setTitle("Upload file");
        Label label = new Label();
        label.setText("File:");

        TextField fileText = new TextField();
//        fileText.setText("test.json");
        Button submitButton = new Button("Submit");
        submitButton.setOnAction(event1 -> {
            fileName = fileText.getText();
            window.close();
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

        return fileName;
    }
}
