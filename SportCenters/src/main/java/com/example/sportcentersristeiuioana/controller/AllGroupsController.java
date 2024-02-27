package com.example.sportcentersristeiuioana.controller;

import com.example.sportcentersristeiuioana.repository.GroupRepository;
import com.example.sportcentersristeiuioana.view.AllGroupsView;
import javafx.scene.control.TableView;

public class AllGroupsController extends ApplicationStartController{
    AllGroupsView groupsView;
    GroupRepository repository;

    public void setGroupsView(AllGroupsView groupsView) {
        this.groupsView = groupsView;
    }

    public void setRepository(GroupRepository repository) {
        this.repository = repository;
    }

    public void setTableData(String searchGroup, ApplicationStartController controller) {
        TableView tableView = groupsView.getTableView();
        tableView.getItems().clear();
        if(searchGroup != null){
            tableView.getItems().addAll(repository.getGroups(searchGroup, controller.getPerson().getPersonId()));
        }
        else
            tableView.getItems().addAll(repository.getAllGroupsDetails(controller.getPerson().getPersonId()));

        tableView.refresh();
    }

    public void pressClearButton() {
        groupsView.getSearchTextField().clear();
    }
    public void confJoinGroup(ApplicationStartController applicationStartController){

    }
    public void showGroups(ApplicationStartController applicationStartController) {

    }
}
