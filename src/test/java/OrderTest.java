import api.OrderApi;
import api.UserApi;
import entity.LoginData;
import entity.OrderRequest;
import entity.User;
import api.UserApiLogin;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class OrderTest {
    private String email;
    private String password;
    private String accessToken;
    private final String validIngredientId = "61c0c5a71d1f82001bdaaa6d"; // Пример валидного ID

    @Before
    public void setUp() {
        RestAssured.baseURI = OrderApi.BASE_URL;
        email = "test_" + RandomStringUtils.randomAlphanumeric(8) + "@example.com";
        password = "password";

        // Создаем пользователя для авторизации
        User user = new User(email, password, "testName");
        UserApi.createUser(user);
        LoginData loginData = new LoginData(email, password);
        Response loginResponse = UserApiLogin.loginUser(loginData);
        accessToken = loginResponse.jsonPath().getString("accessToken");
    }

    @After
    public void tearDown() {
        if (accessToken != null) {
            UserApi.deleteUser("Bearer " + accessToken);
        }
    }

    @Test
    public void testCreateOrderWithAuthorization() { // тест создания заказа с авторизацией
        OrderRequest orderRequest = new OrderRequest(List.of(validIngredientId));
        Response response = OrderApi.createOrder(orderRequest, accessToken);
        response.then().statusCode(SC_OK)
                .body("success", equalTo(true))
                .body("order.number", notNullValue());
    }

    @Test
    public void testCreateOrderWithoutAuthorization() { // куратор сказал: без авторизации должна выходить 401, тест падает
        OrderRequest orderRequest = new OrderRequest(null);
        Response response = OrderApi.createOrder(orderRequest, null);
        response.then().statusCode(SC_UNAUTHORIZED)
                .body("success", equalTo(true))
                .body("message", equalTo("Authorization token is missing or invalid"));
    }

    @Test
    public void testCreateOrderWithIngredients() { // заказ с ингридиентами
        OrderRequest orderRequest = new OrderRequest(List.of(validIngredientId));
        Response response = OrderApi.createOrder(orderRequest, accessToken);
        response.then().statusCode(SC_OK)
                .body("success", equalTo(true))
                .body("order.number", notNullValue());
    }

    @Test
    public void testCreateOrderWithoutIngredients() { // без ингридиентов
        OrderRequest orderRequest = new OrderRequest(null);
        Response response = OrderApi.createOrder(orderRequest, accessToken);
        response.then().statusCode(SC_BAD_REQUEST)
                .body("success", equalTo(false))
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    public void testCreateOrderWithInvalidIngredients() { // с неправильным хэшем
        OrderRequest orderRequest = new OrderRequest(List.of("invalid"));
        Response response = OrderApi.createOrder(orderRequest, accessToken);
        response.then().statusCode(SC_INTERNAL_SERVER_ERROR);
    }
}
