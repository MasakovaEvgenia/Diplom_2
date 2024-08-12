import api.UserApi;
import api.UserApiLogin;
import entity.LoginData;
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

public class UserLoginTest {
    private String email;
    private String password;
    private String accessToken;

    @Before
    public void setUp() {
        RestAssured.baseURI = UserApi.BASE_URL;
        email = "test_" + RandomStringUtils.randomAlphanumeric(8) + "@example.com";
        password = "password";
    }

    @After
    public void tearDown() {
        if (accessToken != null) {
            UserApi.deleteUser("Bearer " + accessToken);
        }
    }

    @Test
    public void testLoginUserWithValidCredentials() {

        // UserApi.createUser(new User(email, password, "testName"));

        LoginData loginData = new LoginData(email, password);
        Response response = UserApiLogin.loginUser(loginData);
        accessToken = response.jsonPath().getString("accessToken");

        response.then().statusCode(SC_OK)
                .body("success", equalTo(true))
                .body("user.email", equalTo(email))
                .body("user.name", notNullValue());
    }

    @Test
    public void testLoginWithIncorrectPassword() {
        UserApi.createUser(new User(email, password, "testName"));

        LoginData loginData = new LoginData(email, "wrongPassword");
        Response response = UserApiLogin.loginUser(loginData);

        response.then().statusCode(SC_UNAUTHORIZED)
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));
    }

    @Test
    public void testLoginWithNonExistentUser() {
        LoginData loginData = new LoginData("nonexistent@example.com", "password");
        Response response = UserApiLogin.loginUser(loginData);

        response.then().statusCode(SC_UNAUTHORIZED)
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));
    }

    @Test
    public void testLoginWithoutEmail() {
        LoginData loginData = new LoginData(null, password);
        Response response = UserApiLogin.loginUser(loginData);

        response.then().statusCode(SC_UNAUTHORIZED)
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));
    }

    @Test
    public void testLoginWithoutPassword() {
        LoginData loginData = new LoginData(email, null);
        Response response = UserApiLogin.loginUser(loginData);

        response.then().statusCode(SC_UNAUTHORIZED)
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));
    }
}
