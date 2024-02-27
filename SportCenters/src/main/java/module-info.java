module com.example.sportcentersristeiuioana {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;

    opens com.example.sportcentersristeiuioana to javafx.fxml;
    exports com.example.sportcentersristeiuioana;
    exports com.example.sportcentersristeiuioana.interfaces;
    opens com.example.sportcentersristeiuioana.interfaces to javafx.fxml;
}