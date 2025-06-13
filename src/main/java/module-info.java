module com.example.fpoebatallanaval {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.fpoebatallanaval to javafx.fxml;
    exports com.example.fpoebatallanaval;
}