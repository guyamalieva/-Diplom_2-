package com.praktikum;

import com.github.javafaker.Faker;

public class UserCredentials {

    public static Faker faker = new Faker();
    public String email;
    public String password;
    public String name;

    public UserCredentials() {
    }

    public UserCredentials(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    public static UserCredentials from(User user) {
        return new UserCredentials(user.email, user.password, user.name);
    }

    public UserCredentials setEmail(String email) {
        this.email = email;
        return this;
    }

    public UserCredentials setName(String name) {
        this.name = name;
        return this;
    }

    public UserCredentials setPassword(String password) {
        this.password = password;
        return this;
    }

    public static UserCredentials getUserWithEmail(User user) {
        return new UserCredentials().setEmail(user.email);
    }

    public static UserCredentials getUserWithPassword(User user) {
        return new UserCredentials().setPassword(user.password);
    }

    public static UserCredentials getUserWithName(User user) {
        return new UserCredentials().setName(user.name);
    }

    public static UserCredentials getUserCredentials() {
        return new UserCredentials().setEmail(faker.internet().emailAddress())
                .setPassword(faker.internet().password())
                .setName(faker.name().name());
    }
}