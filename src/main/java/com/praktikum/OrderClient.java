package com.praktikum;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class OrderClient extends RestAssuredClient {
    private static final String ORDER_PATH = "api/orders";

    @Step("Создание заказа")
    public ValidatableResponse createOrder(IngredientsData ingredients, String token) {
        return given()
                .headers("Authorization", token)
                .spec(getBaseSpec())
                .body(ingredients)
                .when()
                .post(ORDER_PATH)
                .then();
    }

    @Step("Получение заказа")
    public ValidatableResponse getOrderList(String token) {
        return given()
                .headers("Authorization", token)
                .spec(getBaseSpec())
                .when()
                .get(ORDER_PATH)
                .then();
    }
}