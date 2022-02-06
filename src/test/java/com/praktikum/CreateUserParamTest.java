package com.praktikum;

import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class CreateUserParamTest {

    private UserClient userClient;
    private final User user;
    private final String message;
    private int expectedStatus;

    @Before
    public void setUp() {
        userClient = new UserClient();
    }

    public CreateUserParamTest(User user, int expectedStatus,  String message) {
        this.user = user;
        this.message = message;
        this.expectedStatus = expectedStatus;
    }
    @Parameterized.Parameters
    public static Object[][] getTestData () {
        return new Object[][] {
                {User.getRandom(), 200, null},
                {User.getUserWithoutEmail(),403,"Email, password and name are required fields"},
                {User.getUserWithoutPassword(),403,"Email, password and name are required fields"},
                {User.getUserWithoutName(),403,"Email, password and name are required fields"}
        };
    }
    @Test
    public void checkCourierNotCreatedWithoutRequiredFields() {

    ValidatableResponse response = new UserClient().create(user);
    int statusCode = response.extract().statusCode();
    String errorMessage = response.extract().path("message");
    assertEquals(expectedStatus, statusCode);
    assertEquals("Некорректное сообщение об ошибке", message, errorMessage);
  }
}



