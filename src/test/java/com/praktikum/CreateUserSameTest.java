package com.praktikum;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;


public class CreateUserSameTest {

    private User user;
    private UserClient userClient;
    public String token;

    @Before
    public void setUp() {
        user = User.getRandom();
        userClient = new UserClient();
    }

    @After
    public void deleteUser() {

        UserClient.delete(token);
    }

    @Test
    @DisplayName("Проверка невозможности создать двух одинаковых пользователя")
    public void checkCreateSameUsersTest() {
        userClient.create(user);
        ValidatableResponse response = userClient.create(user);
        int statusCode = response.extract().statusCode();
        boolean isSameUserNotCreated = response.extract().path("message").equals("User already exists");
        assertEquals(403, statusCode);
        assertTrue("Пользователь уже существует", isSameUserNotCreated);
    }
}
