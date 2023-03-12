package com.example.mexpense;

public class UserLoginModel {

    private int ID;
    private String firstName;
    private String lastName;
    private String Company;
    private String DOB;
    private String email;
    private Integer phoneNumber;
    private String username;
    private String password;

    public UserLoginModel(int id, String firstName, String lastName, String company, String dob, String email, Integer phoneNumber, String username, String password) {
        ID = id;
        this.firstName = firstName;
        this.lastName = lastName;
        Company = company;
        DOB = dob;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.username = username;
        this.password = password;
    }

    public UserLoginModel() {

    }

    public String toString()
    {
        return  "\nID: "+ ID +
                "\nFirst Name: " + firstName +
                "\nLast Name: " + lastName +
                "\nCompany: " + Company +
                "\nEmail: " + email +
                "\nDOB: " + DOB +
                "\nPhone Number: " + phoneNumber +
                "\nUsername: " + username +
                "\nPassword: " + password + "\n";
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCompany() {
        return Company;
    }

    public void setCompany(String company) {
        Company = company;
    }

    public String getDOB() {
        return DOB;
    }

    public void setDOB(String DOB) {
        this.DOB = DOB;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(Integer phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
