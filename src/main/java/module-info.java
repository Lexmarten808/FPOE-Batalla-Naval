module com.example.fpoebatallanaval {
    requires javafx.controls;
    requires javafx.fxml;
    requires jdk.compiler;


    opens com.example.fpoebatallanaval to javafx.fxml;
    exports com.example.fpoebatallanaval;
    exports com.example.fpoebatallanaval.controller;
    opens com.example.fpoebatallanaval.controller to javafx.fxml;
}