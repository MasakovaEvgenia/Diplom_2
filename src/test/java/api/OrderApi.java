package api;

import entity.OrderRequest;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class OrderApi {
    public static final String BASE_URL = "https://stellarburgers.nomoreparties.site";
    public static final String ORDER_ENDPOINT = "/api/orders";

    @Step("Create order with ingredients")
    public static Response createOrder(OrderRequest orderRequest, String token) {
        return given()
                .header("Content-type", "application/json")
                .header("Authorization", token != null ? token : "")
                .body(orderRequest)
                .when()
                .post(BASE_URL + ORDER_ENDPOINT);
    }

    @Step("Create order with invalid ingredients")
    public static Response createOrderWithInvalidIngredients(String token) {
        return given()
                .header("Content-type", "application/json")
                .header("Authorization", token != null ? token : "")
                .body("{\"ingredients\": [\"invalid_hash\"]}")
                .when()
                .post(BASE_URL + ORDER_ENDPOINT);
    }
}
