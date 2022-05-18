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
import uml.ija.model.SequenceMessage;
import uml.ija.model.UMLAttribute;
import uml.ija.model.UMLOperation;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Trida pro pridani parametru
 *
 * @author Tomáš Polívka (xpoliv06)
 * @author Vojtěch Hájek (xhajek51)
 */
public class CreateSqParController {

    /**
     * Funcke pro vykresleni okna pro pridani parametru do operace
     *
     * @param umlOperation Uml operace
     * @param message      Zprava
     * @return boolean
     */
    public static boolean display(UMLOperation umlOperation, SequenceMessage message) {
        AtomicBoolean result = new AtomicBoolean(false);
        Stage window = new Stage();

        window.setTitle("Add message Arguments");
        Label labelArg = new Label("Add message Arguments:");
        labelArg.setPadding(new Insets(10, 0, 10, 0));
        VBox vBox = new VBox();
        vBox.getChildren().add(labelArg);

        for (UMLAttribute umlAttribute : umlOperation.getArguments()) {
            Label seqArgLabel = new Label();
            seqArgLabel.setText(umlAttribute.toStringClass());
            seqArgLabel.setPadding(new Insets(5, 10, 10, 20));

            TextField seqArgText = new TextField();

            HBox selectBox = new HBox();
            selectBox.getChildren().addAll(seqArgLabel, seqArgText);

            vBox.getChildren().add(selectBox);
        }

        Button submit = new Button("Add arguments");
        submit.setOnMouseClicked(event -> {
            String messageText = message.getOperation() + "( \"";

            boolean valid = true;
            int i = 1;
            for (UMLAttribute umlAttribute : umlOperation.getArguments()) {
                HBox tmp = (HBox) vBox.getChildren().get(i);
                TextField label = (TextField) tmp.getChildren().get(1);
                if (label.getText() == null || label.getText().trim().isEmpty()) {
                    valid = false;
                } else {
                    messageText = messageText + label.getText() + "\", \"";
                }
                i++;
            }
            if (valid) {
                messageText = messageText.substring(0, messageText.length() - 3);
                messageText = messageText + " ) : " + umlOperation.getType().getName();
                message.setMessage(messageText);
                result.set(true);
                window.close();
            }
        });

        vBox.getChildren().add(submit);
        vBox.setAlignment(Pos.CENTER);
        vBox.setPadding(new Insets(20));
        Scene scene = new Scene(vBox);
        window.setScene(scene);
        window.showAndWait();
        return result.get();
    }
}
