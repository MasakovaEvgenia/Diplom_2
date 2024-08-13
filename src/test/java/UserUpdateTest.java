import api.UserApi;
import entity.User;
import entity.UserUpdateRequest;
import entity.LoginData;
import api.ChangeUserApi;
import api.UserApiLogin;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;

public class UserUpdateTest {
    public String email;
    public String password;
    public String accessToken;

    @Before
    public void setUp() {
        RestAssured.baseURI = UserApi.BASE_URL;
        email = "test_" + RandomStringUtils.randomAlphanumeric(8) + "@example.com";
        password = "password";

        // Создаем пользователя для авторизации
        // UserApi.createUser(new User(email, password, "testName"));
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
    public void testUpdateUserWithAuthorization() {
        UserUpdateRequest updateRequest = new UserUpdateRequest("newName", "newemail@example.com");
        Response response = ChangeUserApi.updateUser(updateRequest, "Bearer " + accessToken);
        response.then().statusCode(SC_OK)
                .body("success", equalTo(true))
                .body("user.name", equalTo("newName"))
                .body("user.email", equalTo("newemail@example.com"));
    }

    @Test
    public void testUpdateUserWithoutAuthorization() {
        UserUpdateRequest updateRequest = new UserUpdateRequest("newName", "newemail@example.com");
        Response response = ChangeUserApi.updateUser(updateRequest, null);
        response.then().statusCode(SC_UNAUTHORIZED)
                .body("success", equalTo(false))
                .body("message", equalTo("You should be authorised"));
    }

    @Test
    public void testUpdateUserWithExistingEmail() {
        // Создаем второго пользователя с таким же email
        String existingEmail = "existing_" + RandomStringUtils.randomAlphanumeric(8) + "@example.com";
        UserApi.createUser(new User(existingEmail, password, "existingUser"));

        // Пытаемся обновить данные на уже существующий email
        UserUpdateRequest updateRequest = new UserUpdateRequest("newName", existingEmail);
        Response response = ChangeUserApi.updateUser(updateRequest, "Bearer " + accessToken);
        response.then().statusCode(SC_FORBIDDEN)
                .body("success", equalTo(false))
                .body("message", equalTo("User with such email already exists"));
    }
}
