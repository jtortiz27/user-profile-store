package com.ortiz.userprofilestore.data.model;


public enum State {
    NEW_JERSEY("NJ"),
    NEW_YORK("NY"),
    TEXAS("TX"),
    FLORIDA("FL");

    private String code;

    State(String code) {
        this.code = code;
    }

}
