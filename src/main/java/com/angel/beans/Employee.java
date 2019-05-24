package com.angel.beans;

/**
 * Just a simple POJO representation for de employee entity
 */
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

    public Employee(String firstName) {
        this.firstName = firstName;
    }

    public Employee() {
    }


    /**
     * @return The actual key for de employee its a UUID random generated string
     */
    public String getId() {
        return id;
    }

    /**
     * @param id The actual key for de employee its a UUID random generated string
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return The firstname of the employeee
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @param firstName The firstname of the employeee
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * @return The middle initial of the employee
     */
    public String getMiddleInitial() {
        return middleInitial;
    }

    /**
     * @param middleInitial The middle initial of the employee
     */
    public void setMiddleInitial(String middleInitial) {
        this.middleInitial = middleInitial;
    }

    /**
     * @return The lastnanme of the employee
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * @param lastName The lastnanme of the employee
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * @return A string representation of the date for birth
     */
    public String getDateOfBirth() {
        return dateOfBirth;
    }

    /**
     * @param dateOfBirth A string representation of the date for birth
     */
    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    /**
     * @return A string representation of the date for employment
     */
    public String getDateOfEmployment() {
        return dateOfEmployment;
    }

    /**
     * @param dateOfEmployment A string representation of the date for employment
     */
    public void setDateOfEmployment(String dateOfEmployment) {
        this.dateOfEmployment = dateOfEmployment;
    }

    /**
     * @return ACTIVE or INACTIVE determines the validity of the record
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status ACTIVE or INACTIVE determines the validity of the record
     */
    public void setStatus(String status) {
        this.status = status;
    }

}
