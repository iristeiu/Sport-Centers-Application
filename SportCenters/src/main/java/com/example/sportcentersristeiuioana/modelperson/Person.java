package com.example.sportcentersristeiuioana.modelperson;

import com.example.sportcentersristeiuioana.enumeration.GenderTypes;
import com.example.sportcentersristeiuioana.enumeration.StatusType;

import java.time.LocalDate;

public class Person {
    private int personId;
    private String email;
    private String password;
    private String lastName;
    private String firstName;
    private LocalDate dob;
    private GenderTypes gender;
    private StatusType status;

    public int getPersonId() {
        return personId;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public String getGender() {
        return gender.toString();
    }

    public void setGender(String gender) {
       switch (gender){
          case "F":
              this.gender = GenderTypes.F;
              break;
          case "M":
              this.gender = GenderTypes.M;
              break;
          default: this.gender = GenderTypes.UNSPECIFIED;
       }
    }

    public StatusType getStatus() {
        return status;
    }

    public void setStatus(String status) {
        if(status.equals("PLAYER"))
            this.status = StatusType.PLAYER;
        else
            this.status =StatusType.ADMIN;
    }
}
