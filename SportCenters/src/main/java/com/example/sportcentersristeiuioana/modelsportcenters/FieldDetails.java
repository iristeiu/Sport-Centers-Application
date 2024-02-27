package com.example.sportcentersristeiuioana.modelsportcenters;

import javafx.beans.property.*;

import java.util.SortedMap;

public class FieldDetails {
    private final SimpleIntegerProperty fieldId;
    private final SimpleStringProperty country;
    private final SimpleStringProperty county;
    private final SimpleStringProperty city;
    private final SimpleStringProperty details;
    private final SimpleFloatProperty pricePerHour;
    private final SimpleStringProperty sportName;
    private final SimpleIntegerProperty maxPlayers;
    private final SimpleStringProperty fieldType;
    private final SimpleBooleanProperty ventLight;
    private final SimpleStringProperty floorSurf;

    public FieldDetails(int fieldId, String country, String county, String city, String details, float pricePerHour, String sportName,int maxPlayers, String fieldType, Boolean ventLight, String floorSurf) {
        this.fieldId = new SimpleIntegerProperty(fieldId);
        this.country = new SimpleStringProperty(country);
        this.county = new SimpleStringProperty(county);
        this.city = new SimpleStringProperty(city);
        this.details = new SimpleStringProperty(details);
        this.pricePerHour = new SimpleFloatProperty(pricePerHour);
        this.sportName = new SimpleStringProperty(sportName);
        this.maxPlayers = new SimpleIntegerProperty(maxPlayers);
        this.fieldType = new SimpleStringProperty(fieldType);
        this.ventLight = new SimpleBooleanProperty(ventLight);
        this.floorSurf = new SimpleStringProperty(floorSurf);
    }

    public int getMaxPlayers() {
        return maxPlayers.get();
    }

    public SimpleIntegerProperty maxPlayersProperty() {
        return maxPlayers;
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers.set(maxPlayers);
    }

    public int getFieldId() {
        return fieldId.get();
    }

    public SimpleIntegerProperty fieldIdProperty() {
        return fieldId;
    }

    public void setFieldId(int fieldId) {
        this.fieldId.set(fieldId);
    }

    public String getCountry() {
        return country.get();
    }

    public SimpleStringProperty countryProperty() {
        return country;
    }

    public void setCountry(String country) {
        this.country.set(country);
    }

    public String getCounty() {
        return county.get();
    }

    public SimpleStringProperty countyProperty() {
        return county;
    }

    public void setCounty(String county) {
        this.county.set(county);
    }

    public String getCity() {
        return city.get();
    }

    public SimpleStringProperty cityProperty() {
        return city;
    }

    public void setCity(String city) {
        this.city.set(city);
    }

    public String getDetails() {
        return details.get();
    }

    public SimpleStringProperty detailsProperty() {
        return details;
    }

    public void setDetails(String details) {
        this.details.set(details);
    }

    public float getPricePerHour() {
        return pricePerHour.get();
    }

    public SimpleFloatProperty pricePerHourProperty() {
        return pricePerHour;
    }

    public void setPricePerHour(float pricePerHour) {
        this.pricePerHour.set(pricePerHour);
    }

    public String getSportName() {
        return sportName.get();
    }

    public SimpleStringProperty sportNameProperty() {
        return sportName;
    }

    public void setSportName(String sportName) {
        this.sportName.set(sportName);
    }

    public String getFieldType() {
        return fieldType.get();
    }

    public SimpleStringProperty fieldTypeProperty() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType.set(fieldType);
    }

    public boolean isVentLight() {
        return ventLight.get();
    }

    public SimpleBooleanProperty ventLightProperty() {
        return ventLight;
    }

    public void setVentLight(boolean ventLight) {
        this.ventLight.set(ventLight);
    }

    public String getFloorSurf() {
        return floorSurf.get();
    }

    public SimpleStringProperty floorSurfProperty() {
        return floorSurf;
    }

    public void setFloorSurf(String floorSurf) {
        this.floorSurf.set(floorSurf);
    }
}
