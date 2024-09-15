package org.example.pluto.model;

import lombok.Getter;

@Getter
public class User {
    private final String id;
    private final String fname;
    private final String lname;
    private final String username;
    private final int age;
    private final String email;
    private final String phone;
    private final String type;
    private final int hashCode;

    public User(String fname, String lname, String username, int age, String email, String phone, String type) {
        this.id = username;
        this.fname = fname;
        this.lname = lname;
        this.username = username;
        this.age = age;
        this.email = email;
        this.phone = phone;
        this.type = type;
        this.hashCode = username.hashCode();
    }
}
