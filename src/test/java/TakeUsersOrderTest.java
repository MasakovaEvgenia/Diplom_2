import api.OrderApi;
import entity.LoginData;
import api.UserApi;
import api.UserApiLogin;
import api.TakeUsersOrderApi;
import entity.User;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
public class TakeUsersOrderTest {
    private String email;
    private String password;
    private String accessToken;

    @Before
    public void setUp() {
        RestAssured.baseURI = OrderApi.BASE_URL;
        email = "test_" + RandomStringUtils.randomAlphanumeric(8) + "@example.com";
        password = "password";

        // Создаем пользователя для авторизации
        UserApi.createUser(new User(email, password, "testName"));
        LoginData loginData = new LoginData(email, password);
        Response loginResponse = UserApiLogin.loginUser(loginData);
        accessToken = loginResponse.jsonPath().getString("accessToken");
    }

    @After
    public void tearDown() {
        if (accessToken != null) {
            UserApi.deleteUser( accessToken);
        }
    }

    @Test
    public void testGetOrdersWithAuthorization() {
        Response response = TakeUsersOrderApi.getOrders(accessToken);
        response.then().statusCode(SC_OK)
                .body("success", equalTo(true))
                .body("orders", notNullValue());
    }

    @Test
    public void testGetOrdersWithoutAuthorization() {
        Response response = TakeUsersOrderApi.getOrdersWithoutAuth();
        response.then().statusCode(SC_UNAUTHORIZED)
                .body("success", equalTo(false))
                .body("message", equalTo("You should be authorised"));
    }
}
