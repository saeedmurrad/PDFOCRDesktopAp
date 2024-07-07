module org.pdfocrexport.com.pdfocrdesktopapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires org.apache.pdfbox;
    requires java.desktop;
    requires java.logging;
    requires tess4j;

    opens org.pdfocrexport.com.pdfocrdesktopapp to javafx.fxml;
    exports org.pdfocrexport.com.pdfocrdesktopapp;
}