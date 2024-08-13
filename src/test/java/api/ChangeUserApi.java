package api;
import entity.UserUpdateRequest;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class ChangeUserApi {
    public static final String BASE_URL = "https://stellarburgers.nomoreparties.site";
    public static final String USER_ENDPOINT = "/api/auth/user";

    @Step("Update user information")
    public static Response updateUser(UserUpdateRequest userUpdateRequest, String token) {
        return given()
                .baseUri(BASE_URL)
                .header("Content-Type", "application/json")
                .header("Authorization", token != null ? token : "")
                .body(userUpdateRequest)
                .when()
                .patch(USER_ENDPOINT);
    }

    @Step("Get user information")
    public static Response getUser(String token) {
        return given()
                .baseUri(BASE_URL)
                .header("Authorization", token != null ? token : "")
                .when()
                .get(USER_ENDPOINT);
    }

    @Step("Delete user")
    public static Response deleteUser(String token) {
        return given()
                .baseUri(BASE_URL)
                .header("Authorization", token != null ? token : "")
                .when()
                .delete(USER_ENDPOINT);
    }
}
