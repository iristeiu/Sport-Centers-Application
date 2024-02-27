package com.example.sportcentersristeiuioana.view;

import com.example.sportcentersristeiuioana.controller.ApplicationStartController;
import com.example.sportcentersristeiuioana.controller.ConfController;
import com.example.sportcentersristeiuioana.modelperson.GroupDetails;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.io.FileNotFoundException;

public class ConfView extends ApplicationStartView{
    Label conf = new Label("Confirmation");
    Label groupNameLabel = new Label("Group Name: ");
    Label reservationDate = new Label("Date: ");
    Label hours = new Label("Hours: ");
    Label sportName = new Label("Sport: ");
    Label addressLabel = new Label("Address: ");
    TextField groupName = new TextField();
    TextField date = new TextField();
    TextField hoursText = new TextField();
    TextField sportType= new TextField();
    TextArea address = new TextArea();
    HBox groupBox = new HBox(groupNameLabel, groupName);
    HBox dateBox = new HBox(reservationDate, date);
    HBox sportBox = new HBox(sportName, sportType);
    HBox hoursBox = new HBox(hours, hoursText);
    HBox addressBox = new HBox(addressLabel, address);
    Button joinGroup = new Button("Join");

    VBox reservationDetails = new VBox(conf, groupBox, dateBox, sportBox, hoursBox, addressBox, joinGroup);
    public ConfView(ConfController confController, ApplicationStartController controller, GroupDetails groupDetails) throws FileNotFoundException {
        super(controller);

        window.setWidth(400);
        window.setHeight(500);
        window.setX(centerX - ((double) 500 / 2));
        window.setY(centerY - ((double) 400 / 2));

        conf.setFont(new Font(20));
        conf.setAlignment(Pos.CENTER);
        conf.setPadding(new Insets(20));

        groupBox.setPadding(new Insets(10));
        dateBox.setPadding(new Insets(10));
        sportBox.setPadding(new Insets(10));
        hoursBox.setPadding(new Insets(10));
        addressBox.setPadding(new Insets(10));
        joinGroup.setPadding(new Insets(10));

        groupBox.setSpacing(10);
        dateBox.setSpacing(10);
        sportBox.setSpacing(10);
        hoursBox.setSpacing(10);
        addressBox.setSpacing(10);

        groupBox.setAlignment(Pos.CENTER);
        dateBox.setAlignment(Pos.CENTER);
        addressBox.setAlignment(Pos.CENTER);
        hoursBox.setAlignment(Pos.CENTER);
        sportBox.setAlignment(Pos.CENTER);
        joinGroup.setAlignment(Pos.CENTER);

        groupName.setEditable(false);
        groupName.setText(groupDetails.getGroupName());
        date.setEditable(false);
        date.setText(groupDetails.getReserDate().toString());
        sportType.setEditable(false);
        sportType.setText(groupDetails.getSportName());
        hoursText.setEditable(false);
        hoursText.setText(groupDetails.getHours());
        address.setEditable(false);
        address.setMaxHeight(40); // Set the desired height
        address.setMaxWidth(200);
        address.setWrapText(true);

        address.setText(groupDetails.getCountry() + ",   "+ groupDetails.getCity() + ",\n " + groupDetails.getAddressDetails());

        joinGroup.setOnAction(e->{
            //window.close();
            try {
                confController.joinButtonPressed(controller);
            } catch (FileNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        } );
        reservationDetails.setAlignment(Pos.CENTER);
        container.getChildren().add(reservationDetails);
    }

    public String getGroupName() {
        return this.groupName.getText();
    }
}
