package site.nomoreparties.stellarburgers.utils;

import io.qameta.allure.Step;
import site.nomoreparties.stellarburgers.models.requests.LoginRequest;
import site.nomoreparties.stellarburgers.models.requests.RegisterRequest;
import site.nomoreparties.stellarburgers.models.responses.UserSuccessResponse;
import site.nomoreparties.stellarburgers.models.responses.orders.GetIngredientsResponse;

import static org.apache.http.HttpStatus.SC_OK;

public class Common {

    @Step("Регистрация пользователя")
    public static UserSuccessResponse sendPostRegister(RegisterRequest registerRequest) {
        return Requests.postRegister(registerRequest)
                .then()
                .statusCode(SC_OK)
                .extract()
                .as(UserSuccessResponse.class);
    }

    @Step("Получение значения accessToken")
    public static String getAccessToken(LoginRequest loginRequest) {
        return Requests.postLogin(loginRequest)
                .then()
                .statusCode(SC_OK)
                .extract()
                .as(UserSuccessResponse.class)
                .getAccessToken();
    }

    @Step("Получение значения ingredientId по индексу")
    public static String getIngredientId(int index) {
        return Requests.getIngredients()
                .then()
                .statusCode(SC_OK)
                .extract()
                .as(GetIngredientsResponse.class).getData()[index].get_id();
    }

}
