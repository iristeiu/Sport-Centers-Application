package com.example.sportcentersristeiuioana.modelsportcenters;

import java.util.ArrayList;

public class Timetable {
    private ArrayList<Days> days = new ArrayList<>(7);

    public ArrayList<Days> getDays() {
        return days;
    }

    public void setDays(ArrayList<Days> days) {
        this.days = days;
    }
}
