package com.praktikum;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;


public class CreateOrderTest {
    List<String> ingredients = new ArrayList<>();
    private User user;
    private UserClient userClient;
    public int orderClient;
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
    @DisplayName("Проверка создания заказа c авторизацией")
    public void checkMakeOrderWithAuthTest() {
        String token = userClient.create(user).extract().path("accessToken");
        ingredients = new IngredientsClient().getIngredients().extract().path("data._id");
        IngredientsData orderIngredients = new IngredientsData(ingredients.get(0));
        ValidatableResponse response = new OrderClient().createOrder(orderIngredients, token);
        int statusCode = response.extract().statusCode();
        boolean isOrderCreation = response.extract().path("success");
        orderClient = response.extract().path("order.number");
        assertEquals(statusCode, 200);
        assertTrue("Заказ создан", isOrderCreation);
    }

    @Test
    @DisplayName("Проверка создания заказа без авторизации")
    public void checkMakeOrderWithoutAuthTestTest() {
        ingredients = new IngredientsClient().getIngredients().extract().path("data._id");
        IngredientsData orderIngredients = new IngredientsData(ingredients.get(0));
        ValidatableResponse response = new OrderClient().createOrder(orderIngredients, "");
        int statusCode = response.extract().statusCode();
        boolean isOrderCreationSuccess = response.extract().path("success");
        int orderNumber = response.extract().path("order.number");
        assertEquals(statusCode, 200);
        assertTrue("Заказ создан", isOrderCreationSuccess);
    }

    @Test
    @DisplayName("Проверка создания заказа без игредиентов")
    public void checkMakeOrderWithoutIngredientsTest() {
        String token = userClient.create(user).extract().path("accessToken");
        IngredientsData orderIngredients = new IngredientsData("");
        ValidatableResponse response = new OrderClient().createOrder(orderIngredients, token);
        int statusCode = response.extract().statusCode();
        boolean isOrderNotCreated = response.extract().path("message").equals("Ingredient ids must be provided");
        assertEquals(statusCode, 400);
        assertTrue("Пустой список ингредиентов", isOrderNotCreated);
    }

    @Test
    @DisplayName("Проверка создания заказа с неверным хешем ингредиентов")
    public void checkMakeOrderWithIncorrectIngredientIdsTest() {
        String token = userClient.create(user).extract().path("accessToken");
        IngredientsData orderIngredients = new IngredientsData("test");
        ValidatableResponse response = new OrderClient().createOrder(orderIngredients, token);
        int statusCode = response.extract().statusCode();
        assertEquals(statusCode, 500);
    }
}