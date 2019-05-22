package com.angel.beans;

public class Employee {

    private String id;
    private String firstName;
    private String middleInitial;
    private String lastName;
    private String dateOfBirth;
    private String dateOfEmployment;
    private String status;

    public Employee(String id, String firstName, String middleInitial, String lastName, String dateOfBirth, String dateOfEmployment, String status) {
        this.id = id;
        this.firstName = firstName;
        this.middleInitial = middleInitial;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.dateOfEmployment = dateOfEmployment;
        this.status = status;
    }
    public Employee(String firstName, String middleInitial, String lastName, String dateOfBirth, String dateOfEmployment, String status) {
        this.firstName = firstName;
        this.middleInitial = middleInitial;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.dateOfEmployment = dateOfEmployment;
        this.status = status;
    }
    public Employee(String firstName){
        this.firstName = firstName;
    }
    public Employee(){ }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleInitial() {
        return middleInitial;
    }

    public void setMiddleInitial(String middleInitial) {
        this.middleInitial = middleInitial;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getDateOfEmployment() {
        return dateOfEmployment;
    }

    public void setDateOfEmployment(String dateOfEmployment) {
        this.dateOfEmployment = dateOfEmployment;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
