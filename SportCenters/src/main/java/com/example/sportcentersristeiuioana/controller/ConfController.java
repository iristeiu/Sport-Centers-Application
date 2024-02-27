package com.example.sportcentersristeiuioana.controller;

import com.example.sportcentersristeiuioana.repository.GroupRepository;
import com.example.sportcentersristeiuioana.view.ConfView;
import com.example.sportcentersristeiuioana.view.MyGroupsView;

import java.io.FileNotFoundException;

public class ConfController {
    ConfView confView;
    GroupRepository repository;

    public void setConfView(ConfView confView) {
        this.confView = confView;
    }

    public void setRepository(GroupRepository repository) {
        this.repository = repository;
    }

    public void joinButtonPressed(ApplicationStartController controllerStart) throws FileNotFoundException {
        repository.addToGroup(confView.getGroupName(), controllerStart.getPerson().getPersonId(), false);
        MyGroupsController controller = new MyGroupsController();
        GroupRepository repository = new GroupRepository();
        MyGroupsView myGroupsView = new MyGroupsView(controllerStart, controller);
        controllerStart.setView(myGroupsView);
        controller.setRepository(repository);
        controller.setView(myGroupsView);
    }
}
