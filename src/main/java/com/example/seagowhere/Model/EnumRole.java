package com.example.seagowhere.Model;

public enum EnumRole {
    USER,
    ADMIN
}

// Note: enum has a default method called name
// which can be used directly to access the above names
// Usages can be found in UserService and RoleService

// Otherwise, for further-customisation, consider using the following:

//    USER("USER"),
//    ADMIN("ADMIN");
//
//    private final String value;
//
//    EnumRole(String value) {
//        this.value = value;
//    }
//
//    public String getValue() {
//        return this.value;
//    }
//
//    @Override
//    public String toString() {
//        return this.getValue();
//    }