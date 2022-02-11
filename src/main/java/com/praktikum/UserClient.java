package com.praktikum;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class UserClient extends RestAssuredClient {

    private static final String USER_PATH = "api/auth";

    @Step("Создание пользователя")
    public ValidatableResponse create(User user) {
        return given()
                .spec(getBaseSpec())
                .body(user)
                .when()
                .post(USER_PATH + "/register")
                .then();
    }

    @Step("Авторизация пользователя")
    public ValidatableResponse login(UserCredentials credentials) {
        return given()
                .spec(getBaseSpec())
                .body(credentials)
                .when()
                .post(USER_PATH + "/login")
                .then();
    }

    @Step("Информация о пользователе")
    public ValidatableResponse userInfoChange(String token, UserCredentials credentials) {
        return given()
                .spec(getBaseSpec())
                .auth().oauth2(token)
                .body(credentials)
                .when()
                .patch(USER_PATH + "/user")
                .then();
    }

    @Step("Удаление пользователя")
    public static void delete(String token) {
        if (token == null) {
            return;
        }
        given()
                .spec(getBaseSpec())
                .auth().oauth2(token)
                .when()
                .delete(USER_PATH + "/user")
                .then();
    }
}