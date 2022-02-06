package com.praktikum;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;

public class OrderListTest {
    private UserClient userClient;
    private User user;
    List<String> ingredients = new ArrayList<>();
    private OrderClient orderClient;
    public String orderIngredients;

    @Before
    public void setUp() {
        userClient = new UserClient();
        user = User.getRandom();
        orderClient = new OrderClient();
        ingredients = new IngredientsClient().getIngredients().extract().path("data._id");
        orderIngredients = ingredients.get(0);
    }
    @Test
    @DisplayName("Проверка получения заказа авторизованным пользователем")
    public void checkGetOrderListWithAuthTest() {
        String token = userClient.create(user).extract().path("accessToken");
        ValidatableResponse responseList = orderClient.getOrderList(token);
        List<Map<String, String >> orderList = responseList.extract().path("orders");
        int statusCode = responseList.extract().statusCode();
        boolean isOrderCreate = responseList.extract().path("success");
        assertThat(statusCode, equalTo(200));
        assertTrue("Заказ не создан", isOrderCreate);
    }
    @Test
    @DisplayName("Проверка получения заказа без авторизации")
    public void checkGetOrderListWithoutAuthTest() {
        ValidatableResponse responseList = orderClient.getOrderList("");
        int statusCode = responseList.extract().statusCode();
        boolean isOrderCreate = responseList.extract().path("message").equals("You should be authorised");
        assertThat(statusCode, equalTo(401));
        assertTrue("Заказ не создан", isOrderCreate);
    }
}