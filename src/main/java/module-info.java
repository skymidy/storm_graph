module com.example.stormpr {
    requires javafx.controls;
    requires javafx.fxml;
    requires kotlin.stdlib;

    requires org.kordamp.ikonli.javafx;
    requires opencv;
    requires org.apache.commons.io;

    opens com.example.stormpr to javafx.fxml;
    exports com.example.stormpr;
}