package com.praktikum;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CreateChangeTest {

    private User user;
    private UserClient userClient;
    public String accessToken;

    @Before
    public void setUp() {
        user = User.getRandom();
        userClient = new UserClient();
        userClient.create(user);
    }

    @After
    public void deleteUser() {

        UserClient.delete(accessToken);
    }

    @Test
    @DisplayName("Проверка изменения данных с авторизацией")
    public void checkCredentialsChangeWithAuthTest() {
        String accessToken = userClient.login(UserCredentials.from(user)).extract().path("accessToken");
        accessToken = StringUtils.remove(accessToken, "Bearer ");
        ValidatableResponse response = userClient.userInfoChange(accessToken, UserCredentials.getUserCredentials());
        int statusCode = response.extract().statusCode();
        boolean isChangesSuccess = response.extract().path("success");
        assertEquals(200, statusCode);
        assertTrue("Информация о пользователе не изменилась", isChangesSuccess);
    }

    @Test
    @DisplayName("Проверка изменения данных без авторизации")
    public void checkCredentialsChangeWithoutAuthTest() {
        ValidatableResponse response = userClient.userInfoChange("", UserCredentials.getUserCredentials());
        int statusCode = response.extract().statusCode();
        boolean isNotChangesSuccess = response.extract().path("message").equals("You should be authorised");
        assertEquals(401, statusCode);
        assertTrue("Информация о пользователе изменилась", isNotChangesSuccess);
    }
}