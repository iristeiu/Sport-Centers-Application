package com.example.sportcentersristeiuioana.view;

import com.example.sportcentersristeiuioana.controller.ApplicationStartController;
import com.example.sportcentersristeiuioana.controller.ReservationController;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.time.LocalTime;

public class ReservationView extends ApplicationStartView{
    Label groupNameLabel = new Label("Group Name: ");
    Label startTimeLabel = new Label("From: ");
    Label endTimeLabel = new Label("To: ");
    Label fieldIdLabel = new Label("Field: ");
    Label sportLabel = new Label("Sport: ");
    Label cityLabel = new Label("City: ");
    Label addressLabel = new Label("Address: ");
    TextField groupName = new TextField();
    TextField fieldId = new TextField();
    TextField sportType= new TextField();
    TextField city = new TextField();
    TextField address = new TextField();
    String[] startTime = setStartTime();
    String[] endTime = setEndTime();
    ComboBox startComboBox = new ComboBox(FXCollections.observableArrayList(startTime));
    ComboBox endComboBox = new ComboBox(FXCollections.observableArrayList(endTime));
    HBox groupBox = new HBox(groupNameLabel, groupName);
    HBox startBox = new HBox(startTimeLabel, startComboBox);
    HBox endBox = new HBox(endTimeLabel, endComboBox);
    HBox fieldBox = new HBox(fieldIdLabel, fieldId);
    HBox sportBox = new HBox(sportLabel, sportType);
    HBox cityBox = new HBox(cityLabel, city);
    HBox addressBox = new HBox(addressLabel, address);
    Button createGroup = new Button("Reserve");

    VBox fieldDetails = new VBox(groupBox, startBox, endBox, fieldBox, sportBox, cityBox, addressBox, createGroup);
    private boolean validFields = true;

    public ReservationView(Stage window, ApplicationStartController controller, ReservationController reservationController) throws FileNotFoundException {
        super(controller);
        this.window.setHeight(680);
        this.window.setWidth(450);
        this.window.setX(centerX - ((double) 400 / 2));
        this.window.setY(centerY - ((double) 680 / 2));


        groupBox.setPadding(new Insets(10));
        startBox.setPadding(new Insets(10));
        endBox.setPadding(new Insets(10));
        fieldBox.setPadding(new Insets(10));
        sportBox.setPadding(new Insets(10));
        cityBox.setPadding(new Insets(10));
        addressBox.setPadding(new Insets(10));
        createGroup.setPadding(new Insets(10));

        groupBox.setSpacing(10);
        startBox.setSpacing(10);
        endBox.setSpacing(10);
        fieldBox.setSpacing(10);
        sportBox.setSpacing(10);
        cityBox.setSpacing(10);
        addressBox.setSpacing(10);

        groupBox.setAlignment(Pos.CENTER);
        startBox.setAlignment(Pos.CENTER);
        endBox.setAlignment(Pos.CENTER);
        fieldBox.setAlignment(Pos.CENTER);
        sportBox.setAlignment(Pos.CENTER);
        cityBox.setAlignment(Pos.CENTER);
        addressBox.setAlignment(Pos.CENTER);
        createGroup.setAlignment(Pos.CENTER);

        groupName.setPromptText("Enter your group name...");
        fieldId.setEditable(false);
        sportType.setEditable(false);
        city.setEditable(false);
        address.setEditable(false);

        createGroup.setOnAction(e->{
            window.close();
            try {
                reservationController.reserveButtonPressed(controller);
            } catch (FileNotFoundException ex) {
                throw new RuntimeException(ex);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        } );
        fieldDetails.setAlignment(Pos.CENTER);
        container.getChildren().add(fieldDetails);
    }

    public String getGroupName() {
        String text = groupName.getText();
        if(text.isEmpty()) {
            validFields = false;
            groupName.setStyle("-fx-border-color: #ffbfd7; -fx-border-width: 2px;");
        }
        else {
            validFields = true;
            sportType.setStyle("");
        }
        return text;
    }

    public LocalTime getStartComboBox() {
        String text = (String) startComboBox.getValue();
        if (text == null) {
            validFields = false;
            startComboBox.setStyle("-fx-border-color: #ffbfd7; -fx-border-width: 2px");
            return null;
        }
        else {
            startComboBox.setStyle("");
            return LocalTime.parse(text);
        }
    }

    public LocalTime getEndComboBox() {
        String text = (String) endComboBox.getValue();
        if (text == null) {
            validFields = false;
            endComboBox.setStyle("-fx-border-color: #ffbfd7; -fx-border-width: 2px");
            return null;
        }
        else {
            endComboBox.setStyle("");
            return LocalTime.parse(text);
        }
    }

    public int getFieldId() {
        return Integer.parseInt(fieldId.getText());
    }

    public void setFieldId(int id) {
        this.fieldId.setText(Integer.toString(id));
    }

    public TextField getSportType() {
        return sportType;
    }

    public void setSportType(String sportType) {
        this.sportType.setText(sportType);
    }

    public TextField getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city.setText(city);
    }

    public TextField getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address.setText(address);
    }

    private String[] setStartTime() {
        return new String[]{"08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00",
                "17:00", "18:00", "19:00", "20:00", "21:00"};
    }

    private String[] setEndTime(){
        return new String[]{"09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00",
                "17:00", "18:00", "19:00", "20:00", "21:00"};
    }

    public boolean isValidFields() {
        return validFields;
    }

}
