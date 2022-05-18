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
 * Trida pro ulozeni diagramu
 *
 * @author Tomáš Polívka (xpoliv06)
 * @author Vojtěch Hájek (xhajek51)
 */
public class SaveController {

    /**
     * Nazev souboru.
     */
    static String fileName;

    /**
     * Funkce pro vykresleni, kam se diagram ma ulozit
     *
     * @return jmeno pro soubor
     */
    public static String display() {
        Stage window = new Stage();
        window.setTitle("Save file");
        Label label = new Label();
        label.setText("File:");

        TextField fileText = new TextField();

        Button submitButton = new Button("Save");
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
