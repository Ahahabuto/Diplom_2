package api;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.List;

import static io.restassured.RestAssured.given;

public class OrderApi {
    private static final String INGREDIENTS_ENDPOINT = "/api/ingredients";
    private static final String ORDERS_ENDPOINT = "/api/orders";
    private static final String ALL_ORDERS_ENDPOINT = "/api/orders/all";

    @Step("Получение данных об ингридиентах")
    public static Response getIngredients() {
        return given()
                .spec(Specifications.requestSpec())
                .when()
                .get(INGREDIENTS_ENDPOINT);
    }

    @Step("Создание заказа")
    public static Response createOrder(String accessToken, List<String> ingredients) {
        RequestSpecification request = given()
                .spec(Specifications.requestSpec())
                .body(new OrderRequest(ingredients));

        if (accessToken != null) {
            request.header("Authorization", accessToken);
        }

        return request
                .when()
                .post(ORDERS_ENDPOINT);
    }

    @Step("Получение заказов пользователя")
    public static Response getUserOrders(String accessToken) {
        RequestSpecification request = given()
                .spec(Specifications.requestSpec());

        if (accessToken != null) {
            request.header("Authorization", accessToken);
        }

        return request
                .when()
                .get(ORDERS_ENDPOINT);
    }

    @Step("Получение всех заказов")
    public static Response getAllOrders() {
        return given()
                .spec(Specifications.requestSpec())
                .when()
                .get(ALL_ORDERS_ENDPOINT);
    }
    private static class OrderRequest {
        private final List<String> ingredients;

        public OrderRequest(List<String> ingredients) {
            this.ingredients = ingredients;
        }

        public List<String> getIngredients() {
            return ingredients;
        }
    }
}