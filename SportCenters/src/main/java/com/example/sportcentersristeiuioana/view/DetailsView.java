package com.example.sportcentersristeiuioana.view;

import com.example.sportcentersristeiuioana.controller.ApplicationStartController;
import com.example.sportcentersristeiuioana.controller.DetailsController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class DetailsView extends ApplicationStartView{
    Label title = new Label("My Profile");
    Label fName =  new Label("First Name");
    TextField firstName = new TextField();
    Label lName = new Label("Last Name");
    TextField lastName = new TextField();
    Label date =  new Label("Date of Birth");
    TextField dob = new TextField();
    Label genderLabel = new Label("Gender");
    TextField gender = new TextField();
    Label emailLabel = new Label("Email");
    TextField emailField = new TextField();
    Label passwordLabel = new Label("Password");
    PasswordField passwordField = new PasswordField();
    Button updateFirstName = new Button();
    Button updateLastName = new Button();
    FileInputStream updatePng = new FileInputStream("src/main/java/com/example/sportcentersristeiuioana/images/updating.png");
    FileInputStream updatePng1 = new FileInputStream("src/main/java/com/example/sportcentersristeiuioana/images/updating.png");
    Image image1 = new Image(updatePng, 20, 20, false, false);
    Image image2 = new Image(updatePng1, 20, 20, false, false);
    ImageView imageView1 = new ImageView(image1);
    ImageView imageView2 = new ImageView(image2);
    HBox hBox1 = new HBox(firstName, updateFirstName);
    VBox name1 = new VBox(fName, hBox1);
    HBox hBox2 = new HBox(lastName, updateLastName);
    VBox name2 = new VBox(lName, hBox2);
    VBox dateOfBirth = new VBox(date, dob);
    VBox genderBox = new VBox(genderLabel, gender);
    VBox email = new VBox(emailLabel,emailField);
    VBox password = new VBox(passwordLabel, passwordField);
    VBox titleBox = new VBox(title);
    Button deleteAccount = new Button();
    VBox deleteBox = new VBox(deleteAccount);
    VBox personDetails = new VBox(titleBox, name1, name2, dateOfBirth, genderBox, email, password, deleteBox);

    public DetailsView(ApplicationStartController controllerStart, DetailsController controller) throws FileNotFoundException {
        super(controllerStart);
        window.setHeight(680);
        window.setWidth(450);
        window.setX(centerX - ((double) 400 / 2));
        window.setY(centerY - ((double) 680 / 2));

        title.getStyleClass().add("app-title");
        title.setAlignment(Pos.CENTER);
        title.setPadding(new Insets(5));

        titleBox.setAlignment(Pos.CENTER);
        titleBox.setPadding(new Insets(5));

        deleteAccount.setText("Delete");
        deleteBox.setAlignment(Pos.BOTTOM_CENTER);
        deleteBox.setPadding(new Insets(30));
        deleteAccount.setPadding(new Insets(15));

        updateFirstName.setGraphic(imageView2);
        updateFirstName.setStyle("-fx-background-color: transparent");
        HBox.setHgrow(updateFirstName, Priority.NEVER);
        HBox.setHgrow(firstName, Priority.ALWAYS);

        updateLastName.setGraphic(imageView1);
        updateLastName.setStyle("-fx-background-color: transparent");
        HBox.setHgrow(updateLastName, Priority.NEVER);
        HBox.setHgrow(lastName, Priority.ALWAYS);


        name1.setPadding(new Insets(5));
        name1.setAlignment(Pos.CENTER);
        name2.setPadding(new Insets(5));
        name2.setAlignment(Pos.CENTER);
        dateOfBirth.setPadding(new Insets(5));
        dateOfBirth.setAlignment(Pos.CENTER);
        genderBox.setPadding(new Insets(5));
        genderBox.setAlignment(Pos.CENTER);
        email.setPadding(new Insets(5));
        email.setAlignment(Pos.CENTER);
        password.setPadding(new Insets(5));
        password.setAlignment(Pos.CENTER);

        lastName.setText(controllerStart.getPerson().getLastName());
        lastName.setAlignment(Pos.CENTER);

        firstName.setText(controllerStart.getPerson().getFirstName());
        firstName.setAlignment(Pos.CENTER);

        emailField.setText(controllerStart.getPerson().getEmail());
        emailField.setEditable(false);
        emailField.setAlignment(Pos.CENTER);

        passwordField.setText(controllerStart.getPerson().getPassword());
        passwordField.setEditable(false);
        passwordField.setAlignment(Pos.CENTER);

        dob.setText(controllerStart.getPerson().getDob().toString());
        dob.setEditable(false);
        dob.setAlignment(Pos.CENTER);

        gender.setText(controllerStart.getPerson().getGender());
        gender.setEditable(false);
        gender.setAlignment(Pos.CENTER);

        deleteAccount.setOnAction(e -> controller.delAccount(controllerStart));
        updateLastName.setOnAction(e -> controller.updateLastName(controllerStart));
        updateFirstName.setOnAction(e -> controller.updateFirstName(controllerStart));

        personDetails.setPadding(new Insets(20));

        container.getChildren().add(personDetails);
    }

    public String getFirstName() {
        if(firstName.getText().isEmpty())
            return null;
        else
            return firstName.getText();
    }

    public String getLastName() {
        if(lastName.getText().isEmpty())
            return null;
        else
            return lastName.getText();
    }
}
