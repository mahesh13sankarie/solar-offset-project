package org.example.server.entity;

//TODO(): need to ensure data for standard account
public class User {
    String email, name, address;

    User(String email, String name, String address) {
        this.email = email;
        this.name = name;
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public String getName() {
        return name;
    }
}
