package com.example.sportcentersristeiuioana.controller;

import com.example.sportcentersristeiuioana.modelperson.Person;
import com.example.sportcentersristeiuioana.repository.PersonRepository;
import com.example.sportcentersristeiuioana.view.AuthentificationView;
import com.example.sportcentersristeiuioana.view.SignUpView;
import com.example.sportcentersristeiuioana.view.StartPageView;
import javafx.scene.control.Alert;


public class AuthentificationController {
    private AuthentificationView view;


    public AuthentificationView getView() {
        return view;
    }

    public void setView(AuthentificationView view) {
        this.view = view;
    }

    public void logInPressed() {
        try{
            String email = view.getEmail();
            String password = view.getPassword();
            PersonRepository personRepository = new PersonRepository();
            int valid = personRepository.verifyAccount(email, password);
            if(valid == 1){
                ApplicationStartController controllerStart =new ApplicationStartController();
                Person person = personRepository.getAllPersonDetails(email,password);
                controllerStart.setPerson(person);
                StartPageController controller = new StartPageController();
                StartPageView startView = new StartPageView(controller, controllerStart);
                view.getWindow().close();
                controller.setView(startView);
                controllerStart.setView(startView);
            }
            else{
                view.correctAccountDetails();
            }

        }catch(Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Exception occurred");
            alert.setContentText("An error occurred: " + e.getMessage());
            alert.showAndWait();
        }
    }

    public void signUpPressed(){
        SignUpController controller = new SignUpController();
        SignUpView view = new SignUpView(controller);
        controller.setView(view);
    }


}

