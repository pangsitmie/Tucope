package com.roundbytes.myportfolio;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class User {
    public String name;
    public String email;
    public String dateOfBirth;
    public String currency;


    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }


    public User(String name, String email, String dateOfBirth, String currency) {
        this.name = name;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
        this.currency = currency;
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

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
