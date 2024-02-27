package com.example.sportcentersristeiuioana.view;

import com.example.sportcentersristeiuioana.controller.SignUpController;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.time.LocalDate;


public class SignUpView {
    private boolean validFields=true;
    Stage signUpWindow = new Stage();
    Label fName =  new Label("First Name");
    TextField firstName = new TextField();
    Label lName = new Label("Last Name");
    TextField lastName = new TextField();
    Label date =  new Label("Date of Birth");
    DatePicker dob = new DatePicker();
    Label genderLabel = new Label("Gender");
    String[] genderList = {"M", "F", "UNSPECIFIED"};
    ComboBox comboBox = new ComboBox(FXCollections.observableArrayList(genderList));
    Label emailLabel = new Label("Email");
    TextField emailField = new TextField();
    Label passwordLabel = new Label("Password");
    PasswordField passwordField = new PasswordField();
    Button signUpButton = new Button("Sign Up");
    VBox name1 = new VBox(fName, firstName);
    VBox name2 = new VBox(lName, lastName);
    VBox dateOfBirth = new VBox(date, dob);
    VBox genderBox = new VBox(genderLabel, comboBox);
    VBox button = new VBox(signUpButton);
    VBox email = new VBox(emailLabel,emailField);
    VBox password = new VBox(passwordLabel, passwordField);
    VBox container = new VBox(name1, name2, dateOfBirth, genderBox, email, password, button);

    public SignUpView(SignUpController controller){
        signUpWindow.setHeight(600);
        signUpWindow.setWidth(400);
        signUpWindow.setTitle("Sign Up");
        firstName.setPromptText("Enter First Name ");
        lastName.setPromptText("Enter Last Name ");
        dob.setPromptText("Select Date Of Birth");
        emailField.setPromptText("Enter Email ");
        passwordField.setPromptText("Set Password");

        name1.setPadding(new Insets(5));
        name2.setPadding(new Insets(5));
        dateOfBirth.setPadding(new Insets(5));
        genderBox.setPadding(new Insets(5));
        email.setPadding(new Insets(5));
        password.setPadding(new Insets(5));
        button.setPadding(new Insets(30));
        VBox.setVgrow(button, Priority.NEVER);

        container.setPadding(new Insets(60));
        container.setAlignment(Pos.TOP_CENTER);
        signUpWindow.setScene(new Scene(container));

        signUpButton.setOnAction(e-> {
            try {
                controller.signUpButtonPressed();
            } catch (FileNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        });
        signUpWindow.show();

        String cssPath = getClass().getResource("/design/stylesFirstPage.css").toExternalForm();
        signUpWindow.getScene().getStylesheets().add(cssPath);

        signUpButton.getStyleClass().add("sign-up");
    }
    public void setEmailError(){
        if (email.getChildren().size() >= 3) {
            Node thirdNode = email.getChildren().get(2); // Index starts from 0
            email.getChildren().remove(thirdNode);
        }
        Text emailError = new Text("Enter a valid email address");
        VBox.setVgrow(emailError, Priority.NEVER);
        emailError.setFont(Font.font("Lora", 12));
        emailError.setFill(Color.LIGHTPINK);
        email.getChildren().add(emailError);
    }
    public String getFirstName() {
        String text = firstName.getText();
        if(text.isEmpty()){
            validFields = false;
            firstName.setStyle("-fx-border-color: #fa8bb8; -fx-border-width: 2px");
        }
        else {
            validFields = true;
            firstName.setStyle("");
        }
        return text;
    }

    public String getLastName() {
        String text = lastName.getText();
        if(text.isEmpty()){
            validFields = false;
            lastName.setStyle("-fx-border-color: #fa8bb8; -fx-border-width: 2px;");
        }
        else {
            lastName.setStyle("");
        }
        return text;
    }

    public String getEmail() {
        String text = emailField.getText();
        if(text.isEmpty()) {
            validFields = false;
            emailField.setStyle("-fx-border-color: #fa8bb8; -fx-border-width: 2px;");
        }
        else {
            emailField.setStyle("");
        }
        return text;
    }

    public String getPassword() {
        String text = passwordField.getText();
        if(text.isEmpty()) {
            validFields = false;
            passwordField.setStyle("-fx-border-color: #fa8bb8; -fx-border-width: 2px;");
        }
        else {
            passwordField.setStyle("");
        }
        return text;
    }

    public LocalDate getDob() {
        LocalDate date = dob.getValue();

        if (date == null || validDob()){
            validFields = false;
            dob.setStyle("-fx-border-color: #fa8bb8; -fx-border-width: 2px;");
        }
        else {
            dob.setStyle("");
        }
        return date;
    }

    public String getGender() {
        String text = (String) comboBox.getValue();
        if (text == null) {
            validFields = false;
            comboBox.setStyle("-fx-border-color: #fa8bb8; -fx-border-width: 2px");
        }
        else {
            comboBox.setStyle("");
        }
        return text;
    }

    public Stage getStage(){
        return signUpWindow;
    }

    public boolean getValidFields() {
        return validFields;
    }

    public boolean validDob(){
        LocalDate localDate = LocalDate.now();
        LocalDate date = dob.getValue();
        if(date != null){
            if(date.isAfter(localDate)) {
                return true;
            }
        }
        return false;
    }
}
