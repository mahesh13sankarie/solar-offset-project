package org.example.server.entity;

import lombok.Getter;
import org.example.server.dto.AccountType;

public class User {
    @Getter String email, name, address;
    AccountType accountType;

    User(String email, String name, String address, AccountType accountType) {
        this.email = email;
        this.name = name;
        this.address = address;
        this.accountType = accountType;
    }
}
