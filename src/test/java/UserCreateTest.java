import api.UserApi;
import entity.User;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;

public class UserCreateTest {

    private String email;
    private String password = "1234";
    private String name = "sanemi";
    private String accessToken;

    @Before
    public void setUp() {
        RestAssured.baseURI = UserApi.BASE_URL;
        email = "juniper_" + RandomStringUtils.randomAlphanumeric(8) + "@example.com";
    }

    @After
    public void tearDown() {
        if (accessToken != null) {
            UserApi.deleteUser(accessToken);
        }
    }

    @Test
    public void testCreateUser() {
        User user = new User(email, password, name);
        Response response = UserApi.createUser(user);
        accessToken = response.jsonPath().getString("accessToken");

        response.then().statusCode(SC_OK)
                .body("success", equalTo(true));
    }

    @Test
    public void testCannotCreateDuplicateUser() {
        User user = new User(email, password, name);
        UserApi.createUser(user); // Создание первого пользователя

        Response responseDuplicate = UserApi.createUser(user);
        responseDuplicate.then().statusCode(SC_FORBIDDEN)
                .body("success", equalTo(false))
                .body("message", equalTo("User already exists"));
    }

    @Test
    public void testCreateUserWithoutEmail() {
        User user = new User(null, password, name);
        Response response = UserApi.createUser(user);
        response.then().statusCode(SC_FORBIDDEN)
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    public void testCreateUserWithoutPassword() {
        User user = new User(email, null, name);
        Response response = UserApi.createUser(user);
        response.then().statusCode(SC_FORBIDDEN)
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    public void testCreateUserWithoutName() {
        User user = new User(email, password, null);
        Response response = UserApi.createUser(user);
        response.then().statusCode(SC_FORBIDDEN)
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }
}
