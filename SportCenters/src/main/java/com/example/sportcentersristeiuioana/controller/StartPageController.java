package com.example.sportcentersristeiuioana.controller;

import com.example.sportcentersristeiuioana.repository.FieldRepository;
import com.example.sportcentersristeiuioana.repository.GroupRepository;
import com.example.sportcentersristeiuioana.view.AllGroupsView;
import com.example.sportcentersristeiuioana.view.FieldView;
import com.example.sportcentersristeiuioana.view.StartPageView;

import java.io.FileNotFoundException;

public class StartPageController extends ApplicationStartController{
    public void createGroupButton(ApplicationStartController controllerStart) throws FileNotFoundException {
        applicationView.getWindow().close();
        FieldRepository repository = new FieldRepository();
        FieldController controller = new FieldController();
        FieldView view = new FieldView(controller, controllerStart);
        controller.setFieldView(view);
        controllerStart.setView(view);
        controller.setRepository(repository);
    }

    public void joinAGroup(ApplicationStartController controllerStart) throws FileNotFoundException {
        applicationView.getWindow().close();
        AllGroupsController controller = new AllGroupsController();
        AllGroupsView view = new AllGroupsView(controllerStart, controller);
        GroupRepository repository = new GroupRepository();
        controllerStart.setView(view);
        controller.setGroupsView(view);
        controller.setRepository(repository);
    }
}
