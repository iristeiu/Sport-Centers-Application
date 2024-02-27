package com.example.sportcentersristeiuioana.modelsportcenters;

public class InsideField extends Field{
    private boolean ventilation;
    private String floorMaterial;

    public InsideField() {
    }

    public boolean isVentilation() {
        return ventilation;
    }

    public void setVentilation(boolean ventilation) {
        this.ventilation = ventilation;
    }

    public String getFloorMaterial() {
        return floorMaterial;
    }

    public void setFloorMaterial(String floorMaterial) {
        this.floorMaterial = floorMaterial;
    }
}
