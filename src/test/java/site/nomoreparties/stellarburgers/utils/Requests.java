package site.nomoreparties.stellarburgers.utils;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import site.nomoreparties.stellarburgers.models.requests.LoginRequest;
import site.nomoreparties.stellarburgers.models.requests.OrdersRequest;
import site.nomoreparties.stellarburgers.models.requests.RegisterRequest;

import static io.restassured.RestAssured.given;
import static site.nomoreparties.stellarburgers.utils.Endpoints.*;

public class Requests {

    @Step("Отправка POST запроса на эндпоинт /api/auth/register. Регистрация пользователя")
    public static Response postRegister(RegisterRequest registerRequest) {
        return given()
                .header("Content-type", "application/json")
                .body(registerRequest)
                .when()
                .post(AUTH_REGISTER);
    }

    @Step("Отправка POST запроса на эндпоинт /api/auth/login. Авторизация пользователя")
    public static Response postLogin(LoginRequest loginRequest) {
        return given()
                .header("Content-type", "application/json")
                .body(loginRequest)
                .when()
                .post(AUTH_LOGIN);
    }

    @Step("Отправка POST запроса на эндпоинт /api/orders. Создание заказа, пользователь не авторизован")
    public static Response postOrders(OrdersRequest ordersRequest) {
        return given()
                .header("Content-type", "application/json")
                .body(ordersRequest)
                .when()
                .post(ORDERS);
    }

    @Step("Отправка POST запроса на эндпоинт /api/orders. Создание заказа, пользователь авторизован")
    public static Response postOrders(OrdersRequest ordersRequest, String accessToken) {
        return given()
                .header("Content-type", "application/json")
                .header("Authorization", accessToken)
                .body(ordersRequest)
                .when()
                .post(ORDERS);
    }

    @Step("Отправка DELETE запроса на эндпоинт /api/auth/user. Удаление курьера")
    public static void deleteUser(String accessToken) {
        given()
                .header("Authorization", accessToken)
                .when()
                .delete(AUTH_USER);
    }

    @Step("Отправка GET запроса на эндпоинт /api/ingredients. Получение данных об ингредиентах")
    public static Response getIngredients() {
        return given()
                .header("Content-type", "application/json")
                .when()
                .get(INGREDIENTS);
    }
}
