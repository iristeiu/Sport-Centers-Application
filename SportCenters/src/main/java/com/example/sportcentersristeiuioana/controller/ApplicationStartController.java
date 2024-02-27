package com.example.sportcentersristeiuioana.controller;

import com.example.sportcentersristeiuioana.interfaces.ApplicationStartInterface;
import com.example.sportcentersristeiuioana.modelperson.Person;
import com.example.sportcentersristeiuioana.repository.FieldRepository;
import com.example.sportcentersristeiuioana.repository.GroupRepository;
import com.example.sportcentersristeiuioana.repository.PersonRepository;
import com.example.sportcentersristeiuioana.view.*;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.FileNotFoundException;

public class ApplicationStartController implements ApplicationStartInterface {
    ApplicationStartView applicationView;
    Person person;

    @Override
    public void setView(ApplicationStartView applicationView) {
        this.applicationView = applicationView;
    }
    @Override
    public void addCenter(ApplicationStartController controllerStart) throws FileNotFoundException {
        applicationView.getWindow().close();
        AddCenterController controller = new AddCenterController();
        AddFieldView view1 = new AddFieldView(controller, controllerStart);
        controller.setAddCenter(view1);
        controllerStart.setView(view1);
    }
    @Override
    public void createStartPage(ApplicationStartController controllerStart) throws FileNotFoundException {
        applicationView.getWindow().close();
        StartPageController controller = new StartPageController();
        StartPageView view = new StartPageView(controller, controllerStart);
        controller.setView(view);
        controllerStart.setView(view);
    }
    @Override
    public void createDetailsPage(ApplicationStartController controllerStart) throws FileNotFoundException {
        applicationView.getWindow().close();
        DetailsController controller = new DetailsController();
        DetailsView view = new DetailsView(controllerStart, controller);
        PersonRepository repository = new PersonRepository();
        controller.setApplicationView(view);
        controller.setRepository(repository);
        controllerStart.setView(view);
    }
    @Override
    public void listMyTeams(ApplicationStartController controllerStart) throws FileNotFoundException {
        applicationView.getWindow().close();
        MyGroupsController controller = new MyGroupsController();
        GroupRepository repository = new GroupRepository();
        MyGroupsView myGroupsView = new MyGroupsView(controllerStart, controller);
        controllerStart.setView(myGroupsView);
        controller.setRepository(repository);
        controller.setView(myGroupsView);
    }
    @Override
    public void createTeam(ApplicationStartController controllerStart) throws FileNotFoundException {
        applicationView.getWindow().close();
        FieldRepository repository = new FieldRepository();
        FieldController controller = new FieldController();
        FieldView view = new FieldView(controller, controllerStart);
        controller.setFieldView(view);
        controllerStart.setView(view);
        controller.setRepository(repository);
    }
    @Override
    public void listAllTeams(ApplicationStartController controllerStart) throws FileNotFoundException {
        applicationView.getWindow().close();
        AllGroupsController controller = new AllGroupsController();
        AllGroupsView view = new AllGroupsView(controllerStart, controller);
        GroupRepository repository = new GroupRepository();
        controllerStart.setView(view);
        controller.setGroupsView(view);
        controller.setRepository(repository);
    }
    @Override
    public void joinTeam(ApplicationStartController controllerStart) throws FileNotFoundException {
        applicationView.getWindow().close();
        AllGroupsController controller = new AllGroupsController();
        AllGroupsView view = new AllGroupsView(controllerStart, controller);
        GroupRepository repository = new GroupRepository();
        controllerStart.setView(view);
        controller.setGroupsView(view);
        controller.setRepository(repository);
    }
    @Override
    public void createAllFields(ApplicationStartController controllerStart) throws FileNotFoundException {
        applicationView.getWindow().close();
        FieldRepository repository = new FieldRepository();
        FieldController controller = new FieldController();
        FieldView view = new FieldView(controller, controllerStart);
        controller.setFieldView(view);
        controllerStart.setView(view);
        controller.setRepository(repository);
    }
    @Override
    public Person getPerson() {
        return person;
    }
    @Override
    public void setPerson(Person person) {
        this.person = person;
    }
    @Override
    public void deleteField(int fieldId) {
        FieldRepository repository = new FieldRepository();
        Stage stage = new Stage();
        Label conf = new Label("Are you Sure?");
        Button okButton = new Button();
        okButton.setText("Ok");
        okButton.setOnAction(e -> {
            stage.close();
            repository.deleteField(fieldId);
            InterrView interrView = new InterrView("Field was deleted!");
        });

        VBox vBox = new VBox(conf, okButton);
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(30);
        Scene scene = new Scene(vBox);
        String cssPath = getClass().getResource("/design/stylesFirstPage.css").toExternalForm();
        scene.getStylesheets().add(cssPath);
        stage.setScene(scene);
        stage.show();
        stage.setHeight(200);
        stage.setWidth(300);
    }
}
