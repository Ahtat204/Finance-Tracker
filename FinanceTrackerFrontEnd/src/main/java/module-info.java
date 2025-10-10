module org.asue24.financetrackerfrontend {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;


    opens org.asue24.financetrackerfrontend to javafx.fxml;
    exports org.asue24.financetrackerfrontend;
    exports org.asue24.financetrackerfrontend.controllers;
    opens org.asue24.financetrackerfrontend.controllers to javafx.fxml;
}