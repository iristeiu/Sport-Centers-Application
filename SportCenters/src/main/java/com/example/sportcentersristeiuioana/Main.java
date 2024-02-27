package com.example.sportcentersristeiuioana;

import com.example.sportcentersristeiuioana.view.ApplicationStartView;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import com.example.sportcentersristeiuioana.view.AuthentificationView;
import com.example.sportcentersristeiuioana.controller.AuthentificationController;


import java.io.IOException;

public class Main extends Application {
    public static String[] args;
    @Override
    public void start(Stage stage) throws IOException {
            AuthentificationController controller = new AuthentificationController();
            AuthentificationView view = new AuthentificationView( controller);
            controller.setView(view);
    }

    public static void main(String[] args) {
        Main.args = args;
        launch();
    }
}