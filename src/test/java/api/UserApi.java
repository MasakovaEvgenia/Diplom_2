package api;

import entity.User;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;
import static org.json.XMLTokener.entity;

public class UserApi {
    public final static String BASE_URL = "https://stellarburgers.nomoreparties.site";
    public final static String REGISTER_ENDPOINT = "/api/auth/register";
    public final static String DELETE_USER_ENDPOINT = "/api/auth/user";

    @Step("Создание пользователя")
    public static Response createUser(User user) {
        return given()
                .header("Content-Type", "application/json")
                .body(user)
                .when()
                .post(BASE_URL + REGISTER_ENDPOINT);
    }

    @Step("Удаление пользователя")
    public static Response deleteUser(String accessToken) {
        return given()
                .header("Authorization", accessToken)
                .when()
                .delete(BASE_URL + DELETE_USER_ENDPOINT);
    }
}