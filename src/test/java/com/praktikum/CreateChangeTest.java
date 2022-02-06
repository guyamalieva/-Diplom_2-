package com.praktikum;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class CreateChangeTest {

    private User user;
    private UserClient userClient;

    @Before
    public void setUp() {
        user = User.getRandom();
        userClient = new UserClient();
        userClient.create(user);
    }

    @Test
    @DisplayName("Проверка изменения данных с авторизацией")
    public void checkCredentialsChangeWithAuthTest() {
        String accessToken = userClient.login(UserCredentials.from(user)).extract().path("accessToken");
        accessToken = StringUtils.remove(accessToken, "Bearer ");
        ValidatableResponse response = userClient.userInfoChange(accessToken, UserCredentials.getUserCredentials());
        int statusCode = response.extract().statusCode();
        boolean isChangesSuccess = response.extract().path("success");
        assertThat(statusCode, equalTo(200));
        assertThat("Информация о пользователе не изменилась", isChangesSuccess);
    }

    @Test
    @DisplayName("Проверка изменения данных без авторизации")
    public void checkCredentialsChangeWithoutAuthTest() {
        ValidatableResponse response = userClient.userInfoChange( "", UserCredentials.getUserCredentials());
        int statusCode = response.extract().statusCode();
        boolean isNotChangesSuccess = response.extract().path("message").equals("You should be authorised");
        assertThat(statusCode, equalTo(401));
        assertThat("Информация о пользователе изменилась", isNotChangesSuccess);
    }
}