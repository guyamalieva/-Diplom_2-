package com.praktikum;

import io.restassured.response.ValidatableResponse;
import static io.restassured.RestAssured.given;

public class UserClient extends RestAssuredClient {

    private static final String USER_PATH = "api/auth";

    public ValidatableResponse create(User user) {
        return given()
                .spec(getBaseSpec())
                .body(user)
                .when()
                .post(USER_PATH + "/register")
                .then();
    }

    public ValidatableResponse login(UserCredentials credentials) {
        return given()
                .spec(getBaseSpec())
                .body(credentials)
                .when()
                .post(USER_PATH + "/login")
                .then();
    }

    public ValidatableResponse userInfoChange(String token, UserCredentials credentials) {
        return given()
                .spec(getBaseSpec())
                .auth().oauth2(token)
                .body(credentials)
                .when()
                .patch(USER_PATH + "/user")
                .then();
    }
}