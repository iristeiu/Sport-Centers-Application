package com.example.sportcentersristeiuioana.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import com.example.sportcentersristeiuioana.controller.AuthentificationController;

public class AuthentificationView {
    Stage window = new Stage();
    Label title = new Label("Log In and Start Running!");
    TextField textFieldEmail = new TextField();
    PasswordField textFieldPass = new PasswordField();
    FileInputStream vis = new FileInputStream("src/main/java/com/example/sportcentersristeiuioana/images/Daco_4810872.png");
    FileInputStream notVis = new FileInputStream("src/main/java/com/example/sportcentersristeiuioana/images/Daco_4812183.png");
    Image image1 = new Image(vis, 20, 10, false, false);
    Image image2 = new Image(notVis, 20, 10, false, false);
    ImageView imageView1 = new ImageView(image1);
    ImageView imageView2 = new ImageView(image2);
    Button passVisible = new Button();
    Button buttonLogIn = new Button("Log In");
    Button buttonSignUp = new Button("Sign Up");
    HBox passwordField = new HBox(textFieldPass, passVisible);
    HBox buttons = new HBox(buttonSignUp, buttonLogIn);
    VBox container = new VBox(title, textFieldEmail, passwordField, buttons);

    public AuthentificationView( AuthentificationController controller) throws FileNotFoundException {
        window.setTitle("Authentification");
        window.setWidth(400);
        window.setHeight(600);

        title.setStyle( "-fx-font: 40px 'Glorious Free'");

        textFieldEmail.setPromptText("Email Address");


        textFieldPass.setPromptText("Password");
        passVisible.setStyle("-fx-background-color: transparent");
        HBox.setHgrow(textFieldPass, Priority.ALWAYS); // This will make the textField expand to fill available space
        HBox.setHgrow(passVisible, Priority.NEVER); // This will prevent the button from growing
        passVisible.setGraphic(imageView1);
        passVisible.setOnAction(e -> {  // set Password visible
            if (textFieldPass.isVisible()) {
                textFieldPass.setVisible(false);
                TextField textField = new TextField(textFieldPass.getText());
                textField.setPromptText("Password");
                textField.setPrefColumnCount(textFieldPass.getText().length());
                passwordField.getChildren().remove(textFieldPass);
                passwordField.getChildren().add(0,textField);
                passVisible.setGraphic(imageView2);
                HBox.setHgrow(textField, Priority.ALWAYS);
            } else {
                passwordField.getChildren().remove(0); // Assuming the password field is the second element in the container
                textFieldPass.setVisible(true);
                passwordField.getChildren().add(0, textFieldPass); // Adding the password field back to the container
                passVisible.setGraphic(imageView1);
            }
        });

        buttonLogIn.setOnAction(actionEvent -> controller.logInPressed());
        buttonSignUp.setOnAction(actionEvent -> controller.signUpPressed());
        buttons.setAlignment(Pos.CENTER);
        buttons.setSpacing(20);

        container.setSpacing(15);
        container.setPadding(new Insets(25));
        container.setAlignment(Pos.CENTER);

        window.setScene(new Scene(container));

        String cssPath = getClass().getResource("/design/stylesFirstPage.css").toExternalForm();
        window.getScene().getStylesheets().add(cssPath);

        buttonLogIn.getStyleClass().add("log-in");
        buttonSignUp.getStyleClass().add("sign-up");

        window.show();
    }

    public String getEmail(){
        return textFieldEmail.getText();
    }
    public String getPassword(){
        return textFieldPass.getText();
    }
    public void correctAccountDetails() {
        Text error = new Text("Enter a valid email or password");
        error.setFill(Color.LIGHTPINK);
        if(container.getChildren().size() > 4)
            container.getChildren().remove(3);
        container.getChildren().add(3,error);
    }

    public Stage getWindow() {
        return window;
    }
}
