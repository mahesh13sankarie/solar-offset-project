package org.example.server.dto;

//TODO(): need to ensure data for standard account
public class UserDto {
    private String email, firstName, lastName, phoneNumber, password, address; //all fields mandatory

    UserDto(String email, String firstName, String lastName, String phoneNumber, String password, String address) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public String getAddress() {
        return address;
    }
}
