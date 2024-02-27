package com.example.sportcentersristeiuioana.view;

import com.example.sportcentersristeiuioana.controller.ApplicationStartController;
import com.example.sportcentersristeiuioana.controller.StartPageController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.FileInputStream;
import java.io.FileNotFoundException;


public class StartPageView extends ApplicationStartView{
    Label title = new Label("Your Options: ");
    Button makeReserv = new Button();
    Button joinGroup = new Button();
    Label reservLabel = new Label("Make a Reservation!");
    Label joinGroupLabel = new Label("Choose your team!");
    FileInputStream joinTeamImage = new FileInputStream("src/main/java/com/example/sportcentersristeiuioana/images/participation (1).png");
    FileInputStream makeReservation = new FileInputStream("src/main/java/com/example/sportcentersristeiuioana/images/calendar.png");
    Image image1 = new Image(joinTeamImage, 120, 100, true, false);
    Image image2 = new Image(makeReservation, 100, 100, false, false);
    ImageView imageView1 = new ImageView(image1);
    ImageView imageView2 = new ImageView(image2);
    VBox create = new VBox(makeReserv, reservLabel);
    VBox join = new VBox(joinGroup, joinGroupLabel);
    HBox options = new HBox(create, join);
    VBox vBox = new VBox(title, options);
    public StartPageView(StartPageController controller, ApplicationStartController controllerStart) throws FileNotFoundException {
        super(controllerStart);
        window.setTitle("StartPage");
        window.setHeight(450);
        window.setWidth(580);
        window.setX(centerX - ((double)  580 / 2));
        window.setY(centerY - ((double) 450 / 2));

        title.getStyleClass().add("app-title");
//        joinGroupLabel.setStyle("-fx-font: 15px 'serif'");
//        reservLabel.setStyle("-fx-font: 15px 'serif'");

        makeReserv.setGraphic(imageView2);
        makeReserv.setPadding(new Insets(10));

        joinGroup.setGraphic(imageView1);
        joinGroup.setPadding(new Insets(10));

        join.setPadding(new Insets(20));
        create.setPadding(new Insets(20));

        makeReserv.setStyle("-fx-background-color: transparent");
        joinGroup.setStyle("-fx-background-color: transparent");

        vBox.setAlignment(Pos.CENTER);
        options.setAlignment(Pos.CENTER);
        vBox.setPadding(new Insets(20));
        vBox.setSpacing(25);
        options.setSpacing(40);

        makeReserv.setOnAction(e -> {
            try {
                controller.createGroupButton(controllerStart);
            } catch (FileNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        });
        joinGroup.setOnAction(e -> {
            try {
                controller.joinAGroup(controllerStart);
            } catch (FileNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        });
        container.getChildren().add(vBox);


    }
}
