module com.example.javamusicplayer {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;


    requires org.kordamp.bootstrapfx.core;

    opens com.example.javamusicplayer to javafx.fxml;
    exports com.example.javamusicplayer;
}