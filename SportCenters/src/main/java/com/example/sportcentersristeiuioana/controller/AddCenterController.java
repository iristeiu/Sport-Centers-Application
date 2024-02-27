package com.example.sportcentersristeiuioana.controller;

import com.example.sportcentersristeiuioana.modelsportcenters.Address;
import com.example.sportcentersristeiuioana.modelsportcenters.InsideField;
import com.example.sportcentersristeiuioana.modelsportcenters.OutsideField;
import com.example.sportcentersristeiuioana.modelsportcenters.Sport;
import com.example.sportcentersristeiuioana.repository.FieldRepository;
import com.example.sportcentersristeiuioana.view.AddFieldView;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.FileNotFoundException;

public class AddCenterController extends ApplicationStartController{
    AddFieldView view;
    FieldRepository repository;

    public AddCenterController() {
        this.repository = new FieldRepository();
    }

    public void addField() {
        Sport sport = view.getSport();
        Address address = view.getAddress();
        InsideField insideField = new InsideField();
        OutsideField outsideField = new OutsideField();
        if(view.getValidFields()) {
            if (view.getInside().isSelected() ) {
                view.getDetails(insideField);
                insideField.setAddress(address);
                insideField.setPricePerHour(view.getPrice());
                repository.insertInsideField(sport, insideField);
            } else {
                view.getDetails(outsideField);
                outsideField.setAddress(address);
                outsideField.setPricePerHour(view.getPrice());
                repository.insertOutsideField(sport, outsideField);
            }
            addedSuccessfully(view.getWindow());
        }
    }
    public void addedSuccessfully(Stage view){
        view.close();
        Stage window = new Stage();
        Button button = new Button("OK");
        Label text = new Label("Field Added Successfully!");
        VBox box1 =  new VBox(text, button);
        window.setWidth(300);
        window.setHeight(200);
        box1.setAlignment(Pos.CENTER);
        button.setOnAction(e -> {
            AddCenterController controller = new AddCenterController();
            ApplicationStartController controller1 = new ApplicationStartController();
            AddFieldView centerView = null;
            try {
                centerView = new AddFieldView(controller, controller1);
            } catch (FileNotFoundException ex) {
                throw new RuntimeException(ex);
            }
            controller.setAddCenter(centerView);
            controller1.setView(centerView);
            window.close();
        });
        window.setScene(new Scene(box1));
        window.show();
    }
    public void setAddCenter(AddFieldView view1) {
        this.view = view1;
    }

}
