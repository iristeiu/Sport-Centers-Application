package com.example.sportcentersristeiuioana.view;

import com.example.sportcentersristeiuioana.controller.AddCenterController;
import com.example.sportcentersristeiuioana.controller.ApplicationStartController;
import com.example.sportcentersristeiuioana.modelsportcenters.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.io.FileNotFoundException;

public class AddFieldView extends ApplicationStartView{
    boolean validFields = true;
    Label sport  = new Label("Sport");
    Label price = new Label ("Price");
    Label address = new Label("Address");
    Label sportTypeLabel = new Label("Sport Type:");
    Label pricePerHourLabel = new Label("Price Per Hour: ");
    Label maxPlayersLabel = new Label("Max Players: ");
    Label countryLabel = new Label("Country: ");
    Label countyLabel = new Label("County: ");
    Label cityLabel = new Label("City: ");
    Label addressDetailsLabel = new Label("Str. (...), Nr. (...), Sector. (...), etc.");
    Label locationLabel = new Label("Location");
    Label floorMaterialLabel = new Label("Floor Material: ");
    Label surfaceNatureLabel = new Label("Surface Nature: ");
    TextField sportType = new TextField();
    TextField country = new TextField();
    TextField county = new TextField();
    TextField city = new TextField();
    TextField details = new TextField();
    TextField maxPlayers = new TextField();
    TextField priceText = new TextField();
    TextField floorMaterialField = new TextField();
    TextField surfaceNatureField = new TextField();
    Button submitButton = new Button("Submit");
    CheckBox outside = new CheckBox("OUTSIDE");
    CheckBox inside = new CheckBox("INSIDE");
    CheckBox ventilation = new CheckBox("ventilation");
    CheckBox nightLights = new CheckBox("night lights");
    VBox floorMaterial = new VBox(floorMaterialLabel, floorMaterialField);
    VBox surfaceNature = new VBox(surfaceNatureLabel, surfaceNatureField);
    VBox sportTypeBox = new VBox(sportTypeLabel, sportType);
    VBox maxPlayersBox = new VBox(maxPlayersLabel, maxPlayers);
    VBox countryBox = new VBox(countryLabel, country);
    VBox countyBox = new VBox(countyLabel, county);
    VBox cityBox = new VBox(cityLabel, city);
    VBox detailsBox = new VBox(addressDetailsLabel, details);
    HBox box1 = new HBox(countryBox, countyBox);
    HBox box2 = new HBox(cityBox, detailsBox);
    HBox box3 = new HBox(outside, inside);
    VBox box4 = new VBox(pricePerHourLabel, priceText);
    VBox addressBox = new VBox(address, box1, box2);
    VBox sportBox = new VBox(sport, sportTypeBox, maxPlayersBox);
    VBox locationBox = new VBox(locationLabel, box3);
    VBox priceBox = new VBox(price, box4);
    VBox containerSpecific = new VBox(sportBox, addressBox,priceBox, locationBox,submitButton);
    public AddFieldView(AddCenterController controller, ApplicationStartController controllerStart) throws FileNotFoundException {
        super(controllerStart);
        window.setTitle("Add New Field");
        window.setHeight(600);
        window.setWidth(430);
        window.setX(centerX - ((double) 600 / 2));
        window.setY(centerY - ((double) 430 / 2));

        address.setAlignment(Pos.TOP_LEFT);
        address.setStyle("-fx-font-size: 14px; ");

        sport.setAlignment(Pos.TOP_LEFT);
        sport.setStyle("-fx-font-size: 14px;");

        locationLabel.setAlignment(Pos.TOP_LEFT);
        locationLabel.setStyle("-fx-font-size: 14px;");

        price.setAlignment(Pos.TOP_LEFT);
        price.setStyle("-fx-font-size: 14px;");

        addressBox.setSpacing(15);
        sportBox.setSpacing(15);
        locationBox.setSpacing(15);
        priceBox.setSpacing(15);

        box1.setSpacing(15);
        box2.setSpacing(15);
        box3.setSpacing(40);

        addressBox.setPadding(new Insets(10));
        sportBox.setPadding(new Insets(10));
        locationBox.setPadding(new Insets(10));
        priceBox.setPadding(new Insets(10));

        country.setEffect(dropShadow);
        county.setEffect(dropShadow);
        city.setEffect(dropShadow);
        details.setEffect(dropShadow);
        sportType.setEffect(dropShadow);
        maxPlayers.setEffect(dropShadow);
        surfaceNature.setEffect(dropShadow);
        floorMaterial.setEffect(dropShadow);
        priceText.setEffect(dropShadow);

        maxPlayers.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                maxPlayers.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
        priceText.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*(\\.\\d*)?")) {
                priceText.setText(newValue.replaceAll("[^\\d*(\\.\\d*)?]", ""));
            }
        });

        inside.selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
            if(isNowSelected){
                outside.setSelected(false);
                if(locationBox.getChildren().size() >= 4){
                    locationBox.getChildren().remove(2,4);
                }
                locationBox.getChildren().addAll(ventilation, floorMaterial);
            }
        });

        outside.selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
            if(isNowSelected){
                inside.setSelected(false);
                if(locationBox.getChildren().size() >= 4){
                    locationBox.getChildren().remove(2,4);
                }
                locationBox.getChildren().addAll(nightLights, surfaceNature);
            }
        });

        submitButton.setAlignment(Pos.CENTER);
        VBox.setVgrow(submitButton, Priority.NEVER);
        submitButton.setOnAction(e -> controller.addField());

        containerSpecific.setPadding(new Insets(20));
        containerSpecific.setAlignment(Pos.CENTER);
        container.getChildren().add(containerSpecific);
        ScrollPane scrollPane = new ScrollPane(container);
        scrollPane.setPrefSize(300, 200);
        Scene scene1 = new Scene(scrollPane, 600, 500);
        String cssPath = getClass().getResource("/design/styles.css").toExternalForm();
        scene1.getStylesheets().add(cssPath);
        window.setScene(scene1);
    }
    public Address getAddress(){
        Address addressField = new Address();
        addressField.setCity(getCity());
        addressField.setCountry(getCountry());
        addressField.setCounty(getCounty());
        addressField.setDetails(getDetails());
        return addressField;
    }
    public Sport getSport(){
        Sport sport1 = new Sport();
        sport1.setSportName(getSportName());
        sport1.setMaxPlayers(getMaxPlayers());
        return sport1;
    }

    private int getMaxPlayers() {
        String text = maxPlayers.getText();
        int nrPlayers;
        if(text.isEmpty())
            nrPlayers = 0;
        else
            nrPlayers =Integer.parseInt(text);
        if(nrPlayers == 0){
            validFields = false;
            maxPlayers.setStyle("-fx-border-color: red; -fx-border-width: 2px");
        }
        else {
            maxPlayers.setStyle("");
        }
        return nrPlayers;
    }

    public float getPrice() {
        String text = priceText.getText();
        float price;
        if(text.isEmpty())
            price = 0;
        else
            price =Float.parseFloat(text);

        if(price == 0){
            validFields = false;
            priceText.setStyle("-fx-border-color: red; -fx-border-width: 2px");
        }
        else {
            priceText.setStyle("");
        }
        return price;
    }

    private String getSportName() {
        String text = sportType.getText();
        if(text.isEmpty()){
            validFields = false;
            sportType.setStyle("-fx-border-color: red; -fx-border-width: 2px");
        }
        else {
            sportType.setStyle("");
        }
        return text;
    }

    private String getCountry() {
        String text = country.getText();
        if(text.isEmpty()){
            validFields = false;
            country.setStyle("-fx-border-color: red; -fx-border-width: 2px");
        }
        else {
            validFields = true;
            country.setStyle("");
        }
        return text;
    }

    public String getCounty() {
        String text = county.getText();
        if(text.isEmpty()){
            validFields = false;
            county.setStyle("-fx-border-color: red; -fx-border-width: 2px");
        }
        else {
            county.setStyle("");
        }
        return text;
    }

    public String getCity() {
        String text = city.getText();
        if(text.isEmpty()){
            validFields = false;
            city.setStyle("-fx-border-color: red; -fx-border-width: 2px");
        }
        else {
            city.setStyle("");
        }
        return text;

    }
    public String getDetails() {
        String text = details.getText();
        if(text.isEmpty()){
            validFields = false;
            details.setStyle("-fx-border-color: red; -fx-border-width: 2px");
        }
        else {
            details.setStyle("");
        }
        return text;
    }

    public String getLocation() {
        String loc;
        if (inside.isSelected()) {
            loc = "INSIDE";
        } else {
            if (outside.isSelected()) {
                loc = "OUTSIDE";
            } else {
                loc = "";
                inside.setStyle("-fx-border-style: red");
                outside.setStyle("-fx-border-style: red");
            }
        }
        return loc;
    }

    public void getDetails(InsideField field){
        field.setVentilation(getVentilationState());
        field.setFloorMaterial(getFloorMaterial());
    }

    public void getDetails(OutsideField field){
        field.setNightLights(getNightLights());
        field.setSurfaceNature(getSurfaceNature());
    }

    public boolean getVentilationState(){
        return ventilation.isSelected();
    }
    public String getFloorMaterial(){
        String text = floorMaterialField.getText();
        if(text == null){
            floorMaterial.setStyle("-fx-border-style: red");
            validFields = false;
        }
        else{
            floorMaterial.setStyle("");
        }
        return text;
    }

    public boolean getNightLights(){
        return nightLights.isSelected();
    }
    public String getSurfaceNature(){
        String text = surfaceNatureField.getText();
        if(text == null){
            surfaceNatureField.setStyle("-fx-border-style: red");
            validFields = false;
        }
        else{
            surfaceNatureField.setStyle("");
        }
        return text;
    }

    public CheckBox getOutside() {
        return outside;
    }

    public void setOutside(CheckBox outside) {
        this.outside = outside;
    }

    public CheckBox getInside() {
        return inside;
    }

    public void setInside(CheckBox inside) {
        this.inside = inside;
    }

    public boolean getValidFields() {
        return validFields;
    }
}
