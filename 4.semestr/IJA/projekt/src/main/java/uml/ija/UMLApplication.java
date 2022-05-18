package uml.ija;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Zakladni trida pro beh aplikace
 *
 * @author Tomáš Polívka (xpoliv06)
 * @author Vojtěch Hájek (xhajek51)
 */
public class UMLApplication extends Application {
    /**
     * Funkce pro vykresleni grafickeho okna
     *
     * @param stage scena
     * @throws IOException vyjimka
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(UMLApplication.class.getResource("class_scene.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1280, 800);
        stage.setTitle("UMLDiagram");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Vychozi funkce pro spusteni aplikace
     *
     * @param args vstupni argumenty
     */
    public static void main(String[] args) {
        launch();
    }
}