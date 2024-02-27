package com.example.sportcentersristeiuioana.modelsportcenters;

public class Address {
    private String country;
    private String county;
    private String city;
    private String details;

    public Address(String country, String county, String city, String details) {
        this.country = country;
        this.county = county;
        this.city = city;
        this.details = details;
    }

    public Address() {

    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
