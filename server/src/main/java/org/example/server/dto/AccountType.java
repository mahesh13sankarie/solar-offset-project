package org.example.server.dto;

public enum AccountType {
    Admin {
        @Override
        int getIdentifier() {
            return 0;
        }

        @Override
        String getRole() {
            return "ADMIN";
        }
    },
    Standard {
        @Override
        int getIdentifier() {
            return 1;
        }
        @Override
        String getRole() {
            return "REGULAR";
        }
    },
    Google {
        @Override
        int getIdentifier() {
            return 2;
        }
        @Override
        String getRole() {
            return "REGULAR";
        }
    };

    abstract int getIdentifier();
    abstract String getRole();

    public static String find(int identifier) {
        for (AccountType type : values()) {
            if (type.getIdentifier() == identifier) {
                return type.getRole();
            }
        }
        throw new IllegalArgumentException("No AccountType with identifier " + identifier);
    }
}
