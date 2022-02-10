package com.praktikum;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class OrderListTest {
    private UserClient userClient;
    private User user;
    List<String> ingredients = new ArrayList<>();
    private OrderClient orderClient;
    public String orderIngredients;
    public String token;

    @Before
    public void setUp() {
        userClient = new UserClient();
        user = User.getRandom();
        orderClient = new OrderClient();
        ingredients = new IngredientsClient().getIngredients().extract().path("data._id");
        orderIngredients = ingredients.get(0);
    }

    @After
    public void deleteUser() {

        UserClient.delete(token);
    }

    @Test
    @DisplayName("Проверка получения заказа авторизованным пользователем")
    public void checkGetOrderListWithAuthTest() {
        String token = userClient.create(user).extract().path("accessToken");
        ValidatableResponse responseList = orderClient.getOrderList(token);
        List<Map<String, String>> orderList = responseList.extract().path("orders");
        int statusCode = responseList.extract().statusCode();
        boolean isOrderCreate = responseList.extract().path("success");
        assertEquals(statusCode, 200);
        assertTrue("Заказ не создан", isOrderCreate);
    }

    @Test
    @DisplayName("Проверка получения заказа без авторизации")
    public void checkGetOrderListWithoutAuthTest() {
        ValidatableResponse responseList = orderClient.getOrderList("");
        int statusCode = responseList.extract().statusCode();
        boolean isOrderCreate = responseList.extract().path("message").equals("You should be authorised");
        assertEquals(statusCode, 401);
        assertTrue("Заказ не создан", isOrderCreate);
    }
}