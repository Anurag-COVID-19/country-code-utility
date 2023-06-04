package com.planet.payments.countrycodeutility.entity;

public class ResponseEntity {

    String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ResponseEntity(String message) {
        this.message = message;
    }
}
