package com.example.sportcentersristeiuioana.modelperson;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import java.util.Date;

public class GroupDetails {
    private final SimpleIntegerProperty reservationId;
    private final SimpleStringProperty groupName;
    private final SimpleObjectProperty<Date> reserDate;
    private final SimpleStringProperty hours;
    private final SimpleStringProperty sportName;
    private final SimpleStringProperty country;
    private final SimpleStringProperty city;
    private final SimpleStringProperty addressDetails;
    private final SimpleIntegerProperty nrOfPlayers;
    private final SimpleBooleanProperty availability;

    public GroupDetails(int reservationId, String groupName, Date reserDate, String hours, String sportName, String country, String city, String addressDetails, int nrOfPlayers, boolean availability) {
        this.reservationId = new SimpleIntegerProperty(reservationId);
        this.groupName = new SimpleStringProperty(groupName);
        this.reserDate = new SimpleObjectProperty<>(reserDate);
        this.hours = new SimpleStringProperty(hours);
        this.sportName = new SimpleStringProperty(sportName);
        this.country = new SimpleStringProperty(country);
        this.city = new SimpleStringProperty(city);
        this.addressDetails = new SimpleStringProperty(addressDetails);
        this.nrOfPlayers = new SimpleIntegerProperty(nrOfPlayers);
        this.availability = new SimpleBooleanProperty(availability);
    }

    public int getReservationId() {
        return reservationId.get();
    }

    public SimpleIntegerProperty reservationIdProperty() {
        return reservationId;
    }

    public void setReservationId(int reservationId) {
        this.reservationId.set(reservationId);
    }

    public SimpleObjectProperty<Date> getReserDateProperty() {
        return reserDate;
    }

    public Date getReserDate() {
        return reserDate.get();
    }

    public void setReserDate(Date reserDate) {
        this.reserDate.set(reserDate);
    }

    public String getHours() {
        return hours.get();
    }

    public SimpleStringProperty hoursProperty() {
        return hours;
    }

    public void setHours(String hours) {
        this.hours.set(hours);
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

    public String getCountry() {
        return country.get();
    }

    public SimpleStringProperty countryProperty() {
        return country;
    }

    public void setCountry(String country) {
        this.country.set(country);
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

    public String getAddressDetails() {
        return addressDetails.get();
    }

    public SimpleStringProperty addressDetailsProperty() {
        return addressDetails;
    }

    public void setAddressDetails(String addressDetails) {
        this.addressDetails.set(addressDetails);
    }

    public boolean isAvailability() {
        return availability.get();
    }

    public String getGroupName() {
        return groupName.get();
    }

    public SimpleStringProperty groupNameProperty() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName.set(groupName);
    }

    public int getNrOfPlayers() {
        return nrOfPlayers.get();
    }

    public SimpleIntegerProperty nrOfPlayersProperty() {
        return nrOfPlayers;
    }

    public void setNrOfPlayers(int nrOfPlayers) {
        this.nrOfPlayers.set(nrOfPlayers);
    }

    public boolean getAvailability() {
        return availability.get();
    }

    public SimpleBooleanProperty availabilityProperty() {
        return availability;
    }

    public void setAvailability(boolean availability) {
        this.availability.set(availability);
    }
}
