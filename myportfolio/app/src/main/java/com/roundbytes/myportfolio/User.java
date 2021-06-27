package com.roundbytes.myportfolio;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class User {
    public String name;
    public String email;
    public String telNumber;
    public String dateOfBirth;


    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }


    public User(String name, String email, String telNumber, String dateOfBirth) {
        this.name = name;
        this.email = email;
        this.telNumber = telNumber;
        this.dateOfBirth = dateOfBirth;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelNumber() {
        return telNumber;
    }

    public void setTelNumber(String telNumber) {
        this.telNumber = telNumber;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
}
