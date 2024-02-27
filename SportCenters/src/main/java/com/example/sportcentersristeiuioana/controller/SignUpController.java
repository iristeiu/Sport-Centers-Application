package com.example.sportcentersristeiuioana.controller;

import com.example.sportcentersristeiuioana.repository.PersonRepository;
import com.example.sportcentersristeiuioana.view.ApplicationStartView;
import com.example.sportcentersristeiuioana.view.AuthentificationView;
import com.example.sportcentersristeiuioana.view.SignUpView;

import java.io.FileNotFoundException;
import java.time.LocalDate;

public class SignUpController {
    private SignUpView view;
    PersonRepository repository;

    public SignUpController() {
        this.repository = new PersonRepository();
    }

    public void signUpButtonPressed() throws FileNotFoundException {
        String firstName = view.getFirstName();
        String lastName = view.getLastName();
        String email = view.getEmail();
        String password = view.getPassword();
        LocalDate dob= view.getDob();
        String gender = view.getGender();

        if(repository.verifyEmail(email)==0 && view.getValidFields()){
            repository.insertPerson(email, password, lastName,firstName, dob, gender);
            view.getStage().close();
        }
        else{
            view.setEmailError();
        }
    }


    public void setView(SignUpView view) {
        this.view = view;
    }
}
