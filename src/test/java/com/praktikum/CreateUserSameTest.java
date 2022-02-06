package com.praktikum;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;

public class CreateUserSameTest {

    private User user;
    private UserClient userClient;

    @Before
    public void setUp() {
        user = User.getRandom();
        userClient = new UserClient();
    }

    @Test
    @DisplayName("Проверка невозможности создать двух одинаковых пользователя")
    public void checkCreateSameUsersTest() {
        userClient.create(user);
        ValidatableResponse response = userClient.create(user);
        int statusCode = response.extract().statusCode();
        boolean isSameUserNotCreated = response.extract().path("message").equals("User already exists");
        assertThat(statusCode, equalTo(403));
        assertTrue("Пользователь уже существует", isSameUserNotCreated);
    }
}
