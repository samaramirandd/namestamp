module namestampapp.namestamp {
    requires javafx.controls;
    requires javafx.fxml;


    opens namestampapp.namestamp to javafx.fxml;
    exports namestampapp.namestamp;
}