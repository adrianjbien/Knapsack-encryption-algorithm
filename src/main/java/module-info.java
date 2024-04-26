module com.example.plecakowy {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens com.example.plecakowy to javafx.fxml;
    exports com.example.plecakowy;
}