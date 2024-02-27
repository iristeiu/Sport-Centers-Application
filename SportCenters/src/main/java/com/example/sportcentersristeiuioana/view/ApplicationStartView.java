package com.example.sportcentersristeiuioana.view;

import com.example.sportcentersristeiuioana.controller.ApplicationStartController;
import com.example.sportcentersristeiuioana.enumeration.StatusType;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class ApplicationStartView extends Application {

    Stage window = new Stage();
    Menu home = new Menu();
    ImageView homeIcon = new ImageView(new Image(new FileInputStream("src/main/java/com/example/sportcentersristeiuioana/images/home.png"), 30, 30, false, false));
    Label homeLabel = new Label("Home", homeIcon);
    Menu profile = new Menu();
    ImageView profileIcon = new ImageView(new Image(new FileInputStream("src/main/java/com/example/sportcentersristeiuioana/images/user.png"), 30, 30, false, false));
    Label profileLabel = new Label("Profile", profileIcon);
    Menu groups = new Menu();
    ImageView groupsIcon = new ImageView(new Image(new FileInputStream("src/main/java/com/example/sportcentersristeiuioana/images/group-chat.png"), 30, 30, false, false));
    Label groupsLabel = new Label("Groups", groupsIcon);
    Menu fields = new Menu();
    ImageView fieldsIcon = new ImageView(new Image(new FileInputStream("src/main/java/com/example/sportcentersristeiuioana/images/football-field.png"), 30, 30, false, false));
    Label fieldsLabel = new Label("Fields", fieldsIcon);
    DropShadow dropShadow = new DropShadow();
    MenuItem details = new MenuItem("Details");
    MenuItem myGroups = new MenuItem("MyTeams");
    MenuItem groupItem = new MenuItem("Teams");
    MenuItem chooseGroup = new MenuItem("Join A Team");
    MenuItem createGroupStart = new MenuItem("Create A Team");
    MenuItem homeItem = new MenuItem("Start Page");
    MenuItem fieldItem = new MenuItem("All Fields");
    MenuItem fieldItemAdd = new MenuItem("Add a Field");
    MenuBar menuBar = new MenuBar();
    VBox container = new VBox();
    Scene scene = new Scene(container);

    double centerX = Screen.getPrimary().getVisualBounds().getWidth() / 2;
    double centerY = Screen.getPrimary().getVisualBounds().getHeight() / 2;

    public ApplicationStartView(ApplicationStartController controller) throws FileNotFoundException {
        profile.setGraphic(profileLabel);
        home.setGraphic(homeLabel);
        groups.setGraphic(groupsLabel);
        fields.setGraphic(fieldsLabel);

        homeItem.setStyle("-fx-text-fill: #000000");
        details.setStyle("-fx-text-fill: #000000");
        myGroups.setStyle("-fx-text-fill: #000000");
        groupItem.setStyle("-fx-text-fill: #000000");
        chooseGroup.setStyle("-fx-text-fill: #000000");
        createGroupStart.setStyle("-fx-text-fill: #000000");
        fieldItem.setStyle("-fx-text-fill: #000000");
        fieldItemAdd.setStyle("-fx-text-fill: #000000");

        home.getItems().add(homeItem);
        profile.getItems().addAll(details, myGroups);
        groups.getItems().addAll(groupItem, chooseGroup, createGroupStart);

        homeItem.addEventHandler(ActionEvent.ACTION, e -> {
            try {
                controller.createStartPage(controller);
            } catch (FileNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        });
        details.addEventHandler(ActionEvent.ACTION, e -> {
            try {
                controller.createDetailsPage(controller);
            } catch (FileNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        });
        myGroups.addEventHandler(ActionEvent.ACTION, e -> {
            try {
                controller.listMyTeams(controller);
            } catch (FileNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        });
        groupItem.addEventHandler(ActionEvent.ACTION, e -> {
            try {
                controller.listAllTeams(controller);
            } catch (FileNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        });
        chooseGroup.addEventHandler(ActionEvent.ACTION, e -> {
            try {
                controller.joinTeam(controller);
            } catch (FileNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        });
        createGroupStart.addEventHandler(ActionEvent.ACTION, e -> {
            try {
                controller.createTeam(controller);
            } catch (FileNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        });
        fieldItemAdd.addEventHandler(ActionEvent.ACTION, e -> {
            try {
                controller.addCenter(controller);
            } catch (FileNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        });
        fieldItem.addEventHandler(ActionEvent.ACTION, e -> {
            try {
                controller.createAllFields(controller);
            } catch (FileNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        });

        dropShadow.setOffsetX(2.0);
        dropShadow.setOffsetY(2.0);
        dropShadow.setColor(javafx.scene.paint.Color.rgb(50, 50, 50, 0.8));

        menuBar.setEffect(dropShadow);
        //menuBar.getStyleClass().add("menu-bar");
        menuBar.getStyleClass().add("menu-bar");

        menuBar.getMenus().addAll(home, profile, groups, fields);
        setStatus(controller.getPerson().getStatus());

        container.getChildren().add(menuBar);
        menuBar.setPadding(new Insets(20));
        String cssPath = getClass().getResource("/design/styles.css").toExternalForm();
        scene.getStylesheets().add(cssPath);
        window.setScene(scene);
        window.show();
    }
    public void setStatus(StatusType status){
         if(status == StatusType.ADMIN)
            fields.getItems().addAll(fieldItem, fieldItemAdd);
         else
            fields.getItems().add(fieldItem);
    }

    public VBox getContainer(){
        return container;
    }

    public Stage getWindow() {
        return window;
    }

    @Override
    public void start(Stage stage) throws Exception {
    }
}
