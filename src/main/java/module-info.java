module org.example.shotgungame {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.shotgungame to javafx.fxml;
    exports org.example.shotgungame;
}