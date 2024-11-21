package com.example.CityCompass.models;

public enum UserRequestStatus {

    REQUESTED,


    APPROVED,

    CANCELLED;

    public boolean equalsIgnoreCase(String status) {
        return true;
    }
}
