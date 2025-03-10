package org.example.server.dto;

public enum AccountType {
    Standard {
        @Override
        int getIdentifier() {
            return 1;
        }
    },
    Google {
        @Override
        int getIdentifier() {
            return 2;
        }
    };

    abstract int getIdentifier();
}
