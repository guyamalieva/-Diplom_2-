package com.praktikum;

import io.restassured.response.ValidatableResponse;
import static io.restassured.RestAssured.given;

public class OrderClient extends RestAssuredClient {
    private static final String ORDER_PATH = "api/orders";

    public ValidatableResponse createOrder(IngredientsData ingredients, String token) {
        return given()
                .headers("Authorization", token)
                .spec(getBaseSpec())
                .body(ingredients)
                .when()
                .post(ORDER_PATH)
                .then();
    }

    public ValidatableResponse getOrderList(String token) {
        return given()
                .headers("Authorization", token)
                .spec(getBaseSpec())
                .when()
                .get(ORDER_PATH)
                .then();
    }
}