package steps;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import ru.yandex.praktikum.Order;

import static io.restassured.RestAssured.given;

public class StepsForOrder {

    private static final String ORDER_ENDPOINT = "api/orders";

    @Step("Create order, POST request to /api/orders")
    public Response createOrder(Order order, String accessToken) {
        return given()
                .header("Content-type", "application/json")
                .header("Authorization", accessToken)
                .body(order)
                .when()
                .post(ORDER_ENDPOINT);
    }

    @Step("Get order, GET request to /api/orders")
    public Response getOrder(String accessToken) {
        return given()
                .header("Content-type", "application/json")
                .header("Authorization", accessToken)
                .get(ORDER_ENDPOINT);
    }
}
