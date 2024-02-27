package com.example.sportcentersristeiuioana.controller;

import com.example.sportcentersristeiuioana.repository.GroupRepository;
import com.example.sportcentersristeiuioana.repository.ReservationRepository;
import com.example.sportcentersristeiuioana.view.MyGroupsView;
import com.example.sportcentersristeiuioana.view.ReservationView;

import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.time.LocalTime;

public class ReservationController extends ApplicationStartController{
    ReservationView reservationView;
    ReservationRepository repository;

    public void reserveButtonPressed(ApplicationStartController controllerStart) throws FileNotFoundException, SQLException {
        String groupName = reservationView.getGroupName();
        int fieldId = reservationView.getFieldId();
        LocalTime start = reservationView.getStartComboBox();
        LocalTime end = reservationView.getEndComboBox();

        if (ReservationRepository.checkReservation(fieldId, start, end)) {
            if (start.isBefore(end) && reservationView.isValidFields()) {
                repository.createReservation(controllerStart.getPerson().getPersonId(), groupName, true, fieldId, start, end);
                reservationView.getWindow().close();
                MyGroupsController controller = new MyGroupsController();
                GroupRepository repository = new GroupRepository();
                MyGroupsView myGroupsView = new MyGroupsView(controllerStart, controller);
                controllerStart.setView(myGroupsView);
                controller.setRepository(repository);
                controller.setView(myGroupsView);
            }
        }
    }

    public void setView(ReservationView view){
        this.reservationView = view;
    }

    public void setRepository(ReservationRepository reservationRepository) {
        this.repository = reservationRepository;
    }
}
