package api;
import entity.OrdersResponse;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
public class TakeUsersOrderApi {
    private static final String BASE_URL = "https://stellarburgers.nomoreparties.site";
    private static final String ORDERS_ENDPOINT = "/api/orders";

    @Step("Get orders for authenticated user")
    public static Response getOrders(String authorizationHeader) {
        return given()
                .baseUri(BASE_URL)
                .header("Authorization", authorizationHeader)
                .when()
                .get(ORDERS_ENDPOINT);
    }

    @Step("Get orders for unauthenticated user")
    public static Response getOrdersWithoutAuth() {
        return given()
                .baseUri(BASE_URL)
                .when()
                .get(ORDERS_ENDPOINT);
    }
}
