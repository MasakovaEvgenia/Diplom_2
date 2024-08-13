package api;

import entity.LoginData;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

public class UserApiLogin {
    public final static String BASE_URL = "https://stellarburgers.nomoreparties.site";
    public final static String LOGIN_ENDPOINT = "/api/auth/login";
    public final static String DELETE_USER_ENDPOINT = "/api/auth/user";

    @Step("Авторизация пользователя")
    public static Response loginUser(LoginData loginData) {
        return given()
                .header("Content-Type", "application/json")
                .body(loginData)
                .when()
                .post(BASE_URL + LOGIN_ENDPOINT);
    }

    @Step("Удаление пользователя")
    public static Response deleteUser(String accessToken) {
        return given()
                .header("Authorization", accessToken)
                .when()
                .delete(BASE_URL + DELETE_USER_ENDPOINT);
    }
}
