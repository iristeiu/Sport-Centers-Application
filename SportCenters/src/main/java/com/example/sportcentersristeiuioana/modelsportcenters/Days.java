package com.example.sportcentersristeiuioana.modelsportcenters;

import com.example.sportcentersristeiuioana.enumeration.DaysType;

public class Days {
    private int startHour;
    private int endHour;
    private DaysType day;

    public int getStartHour() {
        return startHour;
    }

    public void setStartHour(int startHour) {
        this.startHour = startHour;
    }

    public int getEndHour() {
        return endHour;
    }

    public void setEndHour(int endHour) {
        this.endHour = endHour;
    }

    public DaysType getDay() {
        return day;
    }

    public void setDay(DaysType day) {
        this.day = day;
    }
}
