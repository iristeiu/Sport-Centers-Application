package com.example.sportcentersristeiuioana.modelsportcenters;

public class Field {
    private Address address;
    private float pricePerHour;
    private Timetable timetable;
    private Sport sport;

    public Field(Address address, float pricePerHour, Timetable timetable, Sport sport) {
        this.address = address;
        this.pricePerHour = pricePerHour;
        this.timetable = timetable;
        this.sport = sport;
    }
    public Field(){

    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public float getPricePerHour() {
        return pricePerHour;
    }

    public void setPricePerHour(float pricePerHour) {
        this.pricePerHour = pricePerHour;
    }

    public Timetable getTimetable() {
        return timetable;
    }

    public void setTimetable(Timetable timetable) {
        this.timetable = timetable;
    }

    public Sport getSport() {
        return sport;
    }

    public void setSport(Sport sport) {
        this.sport = sport;
    }
}
