package com.example.sportcentersristeiuioana.controller;

import com.example.sportcentersristeiuioana.repository.PersonRepository;
import com.example.sportcentersristeiuioana.view.AuthentificationView;
import com.example.sportcentersristeiuioana.view.DetailsView;
import com.example.sportcentersristeiuioana.view.InterrView;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.FileNotFoundException;

public class DetailsController extends ApplicationStartController{
    DetailsView view;
    PersonRepository repository;
    public void setApplicationView(DetailsView view){
        this.view = view;
    }
    public void setRepository(PersonRepository repository){
        this.repository = repository;
    }

    public void delAccount(ApplicationStartController controller){
        Stage stage = new Stage();
        Label conf = new Label("Are you Sure?");
        Button okButton = new Button();
        okButton.setText("Ok");
        okButton.setOnAction(e -> {
            stage.close();
            try {
                confDelete(controller);
            } catch (FileNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        });

        VBox vBox = new VBox(conf, okButton);
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(30);
        Scene scene = new Scene(vBox);
        String cssPath = getClass().getResource("/design/stylesFirstPage.css").toExternalForm();
        scene.getStylesheets().add(cssPath);
        stage.setScene(scene);
        stage.show();
        stage.setHeight(200);
        stage.setWidth(300);
    }

    public void confDelete(ApplicationStartController startController) throws FileNotFoundException {
        if(repository.deleteAccount(startController.getPerson().getPersonId())){
            Stage stage = new Stage();
            Label conf = new Label("Account Deleted!");

            Button okButton = new Button();
            okButton.setText("Ok");
            okButton.setOnAction(e -> stage.close());
            VBox vBox = new VBox(conf, okButton);
            vBox.setAlignment(Pos.CENTER);
            vBox.setSpacing(30);
            Scene scene = new Scene(vBox);
            String cssPath = getClass().getResource("/design/stylesFirstPage.css").toExternalForm();
            scene.getStylesheets().add(cssPath);
            stage.setScene(scene);
            stage.show();
            stage.setHeight(200);
            stage.setWidth(300);
            view.getWindow().close();

            AuthentificationController controller = new AuthentificationController();
            AuthentificationView view = new AuthentificationView( controller);
            controller.setView(view);
        }
    }

    public void updateLastName(ApplicationStartController controllerStart) {
        if(view.getLastName() != null) {
            if(repository.updateLastName(controllerStart.getPerson().getPersonId(), view.getLastName())) {
                controllerStart.getPerson().setLastName(view.getLastName());
                InterrView interrView = new InterrView("Last Name Updated!");
            }
            else{
                InterrView interrView = new InterrView("Not Updated!");
            }
        }
    }

    public void updateFirstName(ApplicationStartController controllerStart) {
        if(view.getLastName() != null)
            if(repository.updateLastName(controllerStart.getPerson().getPersonId(), view.getFirstName())) {
                controllerStart.getPerson().setFirstName(view.getFirstName());
                InterrView interrView = new InterrView("First Name Updated!");
            }
            else{
                InterrView interrView = new InterrView("Not Updated!");
            }
    }
}
