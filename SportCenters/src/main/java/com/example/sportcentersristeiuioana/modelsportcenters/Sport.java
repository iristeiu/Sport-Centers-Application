package com.example.sportcentersristeiuioana.modelsportcenters;

public class Sport {
    private String sportName;
    private int maxPlayers;

    public Sport(String sportName, int maxPlayers) {
        this.sportName = sportName;
        this.maxPlayers = maxPlayers;
    }

    public Sport() {

    }

    public String getSportName() {
        return sportName;
    }

    public void setSportName(String sportName) {
        this.sportName = sportName;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }
}
