module com.example.typingapppro {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires javafx.media;
    requires com.jfoenix;


    opens com.example.typingapppro to javafx.fxml;
    exports com.example.typingapppro;
}