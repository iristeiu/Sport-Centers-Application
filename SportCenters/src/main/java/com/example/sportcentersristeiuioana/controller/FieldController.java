package com.example.sportcentersristeiuioana.controller;

import com.example.sportcentersristeiuioana.repository.FieldRepository;
import com.example.sportcentersristeiuioana.view.FieldView;
import javafx.scene.control.TableView;

public class FieldController extends ApplicationStartController{
    FieldRepository repository;
    FieldView fieldView;
    public void setTableData(String city, String loc, String sport) {
        TableView tableView = fieldView.getTableView();
        tableView.getItems().clear();
        if (city != null) {
            if (loc != null) {
                if (sport != null) {
                    // Filter by city, loc, and sport
                    tableView.getItems().addAll(repository.getCityLocSport(city, loc, sport));
                } else {
                    // Filter by city and loc
                    tableView.getItems().addAll(repository.getCityLoc(city, loc));
                }
            } else {
                if (sport != null) {
                    // Filter by city, loc, and sport
                    tableView.getItems().addAll(repository.getCitySport(city,sport));
                } else {
                    // Filter by city and loc
                    tableView.getItems().addAll(repository.getCity(city));
                }
            }
        } else {
            if (loc != null) {
                // Filter by loc
                if (sport != null) {
                    // Filter by sport
                    tableView.getItems().addAll(repository.getLocSport(loc, sport));
                } else {
                    // No filters, add all data
                    tableView.getItems().addAll(repository.getLoc(loc));
                }
            } else {
                if (sport != null) {
                    // Filter by sport
                    tableView.getItems().addAll(repository.getSportSearch(sport));
                } else {
                    // No filters, add all data
                    tableView.getItems().addAll(repository.getAllFieldDetails());
                }
            }
        }
        tableView.refresh();
    }
    public void pressClearButton(){
        fieldView.getSearchField().clear();
        fieldView.getSport().getSelectionModel().clearSelection();
        fieldView.getSearchLocation().clear();
    }
    public void setRepository(FieldRepository repository) {
        this.repository = repository;
    }

    public void setFieldView(FieldView fieldView) {
        this.fieldView = fieldView;
    }
}
