package entity;

import java.util.Date;

public class Customer {
    private int customerID;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String address;
    private String username;
    private String password;
    private Date registrationDate;

    // Default Constructor
    public Customer() {}

    // Parameterized Constructor
    public Customer(int customerID, String firstName, String lastName, String email, String phoneNumber, 
                    String address, String username, String password, Date registrationDate) {
        this.customerID = customerID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.username = username;
        this.password = password;
        this.registrationDate = registrationDate;
    }

    // Getters and Setters for each field
    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    // Method to authenticate user by password
    public boolean authenticate(String password) {
        return this.password.equals(password);
    }
}
