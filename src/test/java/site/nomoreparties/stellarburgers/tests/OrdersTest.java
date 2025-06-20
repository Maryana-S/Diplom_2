package site.nomoreparties.stellarburgers.tests;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import site.nomoreparties.stellarburgers.models.requests.LoginRequest;
import site.nomoreparties.stellarburgers.models.requests.OrdersRequest;
import site.nomoreparties.stellarburgers.models.requests.RegisterRequest;
import site.nomoreparties.stellarburgers.models.responses.FailureResponse;
import site.nomoreparties.stellarburgers.models.responses.UserSuccessResponse;
import site.nomoreparties.stellarburgers.models.responses.orders.PostOrderSuccessResponse;
import site.nomoreparties.stellarburgers.utils.Requests;

import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.*;
import static site.nomoreparties.stellarburgers.utils.Common.*;
import static site.nomoreparties.stellarburgers.utils.Constants.NO_INGREDIENT;

public class OrdersTest extends BaseTest {

    String email;
    String password;
    String name;

    RegisterRequest registerRequest;
    LoginRequest loginRequest;
    UserSuccessResponse registerSuccessResponse;

    @Before
    @Step("Инициализация параметров: email, password, name, registerRequest, loginRequest, registerSuccessResponse")
    public void initParams() {
        email = RandomStringUtils.randomAlphabetic(5) + "@ya.ru";
        password = RandomStringUtils.randomAlphanumeric(9);
        name = RandomStringUtils.randomAlphabetic(7);
        registerRequest = new RegisterRequest(email, password, name);
        loginRequest = new LoginRequest(email, password);
        registerSuccessResponse = sendPostRegister(registerRequest);
    }

    @Test
    @DisplayName("Успешное создание заказа с авторизацией пользователя в системе, проверка соответсятвия значений email пользователя и id ингредиента")
    @Description("Отправка POST-запрса на эндпоинт /api/orders с авторизацией пользователя в системе")
    public void createOrderLoginUserSuccessTest() {
        String ingredientId = getIngredientId(0);
        OrdersRequest ordersRequest = new OrdersRequest(new String[]{ingredientId});
        PostOrderSuccessResponse postOrdersSuccessResponse = Requests.postOrders(ordersRequest, getAccessToken(loginRequest))
                .then()
                .statusCode(SC_OK)
                .extract()
                .as(PostOrderSuccessResponse.class);

        assertTrue("Вернулось значение false в поле 'success'", postOrdersSuccessResponse.isSuccess());
        assertEquals("ID ингредиента в ответе не соответствует переданному в запросе", ingredientId, postOrdersSuccessResponse.getOrder().getIngredients()[0].get_id());
        assertEquals("email пользователя в ответе не соответствует переданному в запросе", email.toLowerCase(), postOrdersSuccessResponse.getOrder().getOwner().getEmail());
    }

    @Test
    @DisplayName("Успешное создание заказа без авторизации пользователя в системе")
    @Description("Отправка POST-запрса на эндпоинт /api/orders без авторизации подьзователя. Проверка, что в ответе возвращается номер заказа")
    public void createOrderWithoutLoginUserSuccessTest() {
        OrdersRequest ordersRequest = new OrdersRequest(new String[]{getIngredientId(0)});
        PostOrderSuccessResponse postOrdersSuccessResponse = Requests.postOrders(ordersRequest)
                .then()
                .statusCode(SC_OK)
                .extract()
                .as(PostOrderSuccessResponse.class);

        assertTrue("Вернулось значение false в поле 'success'", postOrdersSuccessResponse.isSuccess());
        assertNotNull("В ответе не пришло значение number 'Номер заказа'", postOrdersSuccessResponse.getOrder().getNumber());
    }

    @Test
    @DisplayName("При создании заказа без ингредиентов возвращается код ответа 400 Bad Request. Пользователь не авторизован")
    @Description("Отправка POST-запрса на эндпоинт /api/orders без авторизации с пустым значением в поле 'ingredients'")
    public void createOrderWithoutIngredientsBadRequestTest() {
        OrdersRequest ordersRequest = new OrdersRequest(new String[]{});
        FailureResponse postOrdersFailureResponse = Requests.postOrders(ordersRequest)
                .then()
                .statusCode(SC_BAD_REQUEST)
                .extract()
                .as(FailureResponse.class);

        assertFalse("Вернулось значение true в поле 'success'", postOrdersFailureResponse.isSuccess());
        assertEquals("Текст поля message в ответе не соответствует ожидаемому", NO_INGREDIENT, postOrdersFailureResponse.getMessage());
    }

    @Test
    @DisplayName("При создании заказа c невалидным хешем ингредиента возвращается код ответа 500 Internal Server Error. Пользователь не авторизован")
    @Description("Отправка POST-запрса на эндпоинт /api/orders без авторизации с невалидным хешем ингредиента в поле 'ingredients'")
    public void createOrderWithIncorrectIngredientErrorTest() {
        OrdersRequest ordersRequest = new OrdersRequest(new String[]{RandomStringUtils.randomAlphanumeric(15)});
        Requests.postOrders(ordersRequest)
                .then()
                .statusCode(SC_INTERNAL_SERVER_ERROR);
    }

    @Test
    @DisplayName("При создании заказа без ингредиентов возвращается код ответа 400 Bad Request. Пользователь авторизован")
    @Description("Отправка POST-запрса на эндпоинт /api/orders с пустым значением в поле 'ingredients'")
    public void createOrderWithoutIngredientsAndLoginBadRequestTest() {
        String accessToken = getAccessToken(loginRequest);
        OrdersRequest ordersRequest = new OrdersRequest(new String[]{});
        FailureResponse postOrdersFailureResponse = Requests.postOrders(ordersRequest, accessToken)
                .then()
                .statusCode(SC_BAD_REQUEST)
                .extract()
                .as(FailureResponse.class);

        assertFalse("Вернулось значение true в поле 'success'", postOrdersFailureResponse.isSuccess());
        assertEquals("Текст поля message в ответе не соответствует ожидаемому", NO_INGREDIENT, postOrdersFailureResponse.getMessage());
    }

    @Test
    @DisplayName("При создании заказа c невалидным хешем ингредиента возвращается код ответа 500 Internal Server Error. Пользователь авторизован")
    @Description("Отправка POST-запрса на эндпоинт /api/orders без авторизации с невалидным хешем ингредиента в поле 'ingredients'")
    public void createOrderWithIncorrectIngredientAndLoginErrorTest() {
        String accessToken = getAccessToken(loginRequest);
        OrdersRequest ordersRequest = new OrdersRequest(new String[]{RandomStringUtils.randomAlphanumeric(15)});
        Requests.postOrders(ordersRequest, accessToken)
                .then()
                .statusCode(SC_INTERNAL_SERVER_ERROR);
    }

    @Test
    @DisplayName("Успешное создание заказа с тремя ингридиентами зарегистрированным пользователем")
    @Description("Отправка POST-запрса на эндпоинт /api/orders авторизованным пользователем, в заказа добавлено существующих 3 ингредиента")
    public void createOrderWithThreeIngredientsWithLoginSuccessTest() {
        OrdersRequest ordersRequest = new OrdersRequest(new String[]{getIngredientId(0), getIngredientId(1), getIngredientId(2)});
        PostOrderSuccessResponse orderSuccessResponse = Requests.postOrders(ordersRequest, getAccessToken(loginRequest))
                .then()
                .statusCode(SC_OK)
                .extract()
                .as(PostOrderSuccessResponse.class);

        assertTrue("Вернулось значение false в поле 'success'", orderSuccessResponse.isSuccess());
        assertEquals(3, orderSuccessResponse.getOrder().getIngredients().length);
    }

    @Test
    @DisplayName("Успешное создание заказа с тремя ингридиентами незарегистрированным пользователем")
    @Description("Отправка POST-запрса на эндпоинт /api/orders неавторизованным пользователем, в заказа добавлено существующих 3 ингредиента")
    public void createOrderWithThreeIngredientsWithoutLoginSuccessTest() {
        OrdersRequest ordersRequest = new OrdersRequest(new String[]{getIngredientId(0), getIngredientId(1), getIngredientId(3)});
        PostOrderSuccessResponse orderSuccessResponse = Requests.postOrders(ordersRequest)
                .then()
                .statusCode(SC_OK)
                .extract()
                .as(PostOrderSuccessResponse.class);
        assertTrue("Вернулось значение false в поле 'success'", orderSuccessResponse.isSuccess());
    }

    @After
    @Step("Удаление созданного пользователя")
    public void deleteUser() {
        Requests.deleteUser(registerSuccessResponse.getAccessToken());
    }
}
