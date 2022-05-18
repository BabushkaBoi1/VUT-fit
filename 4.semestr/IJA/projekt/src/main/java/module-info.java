module uml.ija {
    requires javafx.controls;
    requires javafx.fxml;
    requires junit;
    requires org.json;
    requires jfxtras.labs;

    opens uml.ija to javafx.fxml;
    exports uml.ija;
}