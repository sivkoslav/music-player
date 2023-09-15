package com.example.javamusicplayer;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class EnterUrl {

    @FXML
    TextField textFieldURL;

    @FXML
    Button btnSubmit;

    public String onSubmit(){

        return textFieldURL.getText();
    }
}
