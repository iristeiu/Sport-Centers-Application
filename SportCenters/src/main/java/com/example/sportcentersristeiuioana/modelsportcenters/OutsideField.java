package com.example.sportcentersristeiuioana.modelsportcenters;

public class OutsideField extends Field{
    private String surfaceNature;
    private boolean nightLights;

    public OutsideField() {
    }

    public String getSurfaceNature() {
        return surfaceNature;
    }

    public void setSurfaceNature(String surfaceNature) {
        this.surfaceNature = surfaceNature;
    }

    public boolean isNightLights() {
        return nightLights;
    }

    public void setNightLights(boolean nightLights) {
        this.nightLights = nightLights;
    }
}
