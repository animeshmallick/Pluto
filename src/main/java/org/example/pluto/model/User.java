package org.example.pluto.model;

import lombok.Getter;

@Getter
public class Login {
    private final String message;
    public Login(String message){
        this.message = message;
    }
}
