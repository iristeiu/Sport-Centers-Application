package com.example.sportcentersristeiuioana.interfaces;

import com.example.sportcentersristeiuioana.controller.*;
import com.example.sportcentersristeiuioana.modelperson.Person;
import com.example.sportcentersristeiuioana.view.*;


import java.io.FileNotFoundException;

public interface ApplicationStartInterface {
    void setView(ApplicationStartView applicationView);
    void addCenter(ApplicationStartController controllerStart) throws FileNotFoundException;
    void createStartPage(ApplicationStartController controllerStart) throws FileNotFoundException;
    void createDetailsPage(ApplicationStartController controllerStart) throws FileNotFoundException;
    void listMyTeams(ApplicationStartController controllerStart) throws FileNotFoundException;
    void createTeam(ApplicationStartController controllerStart) throws FileNotFoundException;
    void listAllTeams(ApplicationStartController controllerStart) throws FileNotFoundException;
    void joinTeam(ApplicationStartController controllerStart) throws FileNotFoundException;
    void createAllFields(ApplicationStartController controllerStart) throws FileNotFoundException;
    Person getPerson();
    void setPerson(Person person);
    void deleteField(int fieldId);
}
