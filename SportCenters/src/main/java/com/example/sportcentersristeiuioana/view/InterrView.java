package com.example.sportcentersristeiuioana.view;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class InterrView {
    Stage stage = new Stage();
    Label messageLabel = new Label();
    Button okButton = new Button();
    VBox vBox = new VBox(messageLabel, okButton);
    Scene scene = new Scene(vBox);
    String cssPath = getClass().getResource("/design/stylesFirstPage.css").toExternalForm();


    public InterrView(String message) {
        okButton.setText("Ok");
        okButton.setOnAction(e -> stage.close());

        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(30);
        messageLabel.setText(message);
        scene.getStylesheets().add(cssPath);
        stage.setScene(scene);
        stage.show();

        stage.setHeight(200);
        stage.setWidth(300);
    }

    public Button getButton() {
        return okButton;
    }

    public Stage getStage() {
        return  stage;
    }
}
