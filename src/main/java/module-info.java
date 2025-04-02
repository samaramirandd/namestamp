module namestampapp.namestamp {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires org.apache.pdfbox;
    requires com.opencsv;


    opens namestampapp.namestamp to javafx.fxml;
    exports namestampapp.namestamp;
}