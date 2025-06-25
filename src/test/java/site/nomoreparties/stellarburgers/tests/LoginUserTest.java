package site.nomoreparties.stellarburgers.tests;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import site.nomoreparties.stellarburgers.models.requests.LoginRequest;
import site.nomoreparties.stellarburgers.models.requests.RegisterRequest;
import site.nomoreparties.stellarburgers.models.responses.FailureResponse;
import site.nomoreparties.stellarburgers.models.responses.UserSuccessResponse;
import site.nomoreparties.stellarburgers.utils.Requests;

import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.*;
import static site.nomoreparties.stellarburgers.utils.Common.sendPostRegister;
import static site.nomoreparties.stellarburgers.utils.Constants.LOGIN_UNAUTHORIZED;

public class LoginUserTest extends BaseTest {

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
    @DisplayName("Успешная авторизация пользователя в системе")
    @Description("Отправка POST запроса на эндпоинт /api/auth/login c параметрами email и password существующего пользователя")
    public void loginExistingUserSuccessTest() {
        UserSuccessResponse loginSuccessResponse = Requests.postLogin(loginRequest)
                .then()
                .statusCode(SC_OK)
                .extract()
                .as(UserSuccessResponse.class);
        assertTrue("Вернулось значение false в поле 'success'", loginSuccessResponse.isSuccess());
    }

    @Test
    @DisplayName("Успешная авторизация пользователя в системе. Проверка, что значения полей name и email в ответе, соответствуют значениям полей в запросе")
    @Description("Отправка POST запроса на эндпоинт /api/auth/login c параметрами email и password существующего пользователя")
    public void loginExistingUserReturnEqualsCredentialsTest() {
        UserSuccessResponse loginSuccessResponse = Requests.postLogin(loginRequest)
                .then()
                .extract()
                .as(UserSuccessResponse.class);
        assertEquals("Значение поля name в ответе отличается от переданного в запросе", name, loginSuccessResponse.getUser().getName());
        assertEquals("Значение поля email в ответе отличается от переданного в запросе", email.toLowerCase(), loginSuccessResponse.getUser().getEmail());
    }

    @Test
    @DisplayName("При автроизации пользователя с некорректным значением в поле password, в ответе возвращается код 401 Unauthorized")
    @Description("Отправка POST запроса на эндпоинт /api/auth/login c некорректным password")
    public void loginIncorrectPasswordUnauthorizedTest() {
        loginRequest.setPassword("IncorrectPass");

        FailureResponse loginUnauthorizedResponse = Requests.postLogin(loginRequest)
                .then()
                .statusCode(SC_UNAUTHORIZED)
                .extract()
                .as(FailureResponse.class);
        assertFalse("Вернулось значение true в поле 'success'", loginUnauthorizedResponse.isSuccess());
        assertEquals("Текст поля message в ответе не соответствует ожидаемому", LOGIN_UNAUTHORIZED, loginUnauthorizedResponse.getMessage());
    }

    @Test
    @DisplayName("При автроизации пользователя с некорректным значением в поле email, в ответе возвращается код 401 Unauthorized")
    @Description("Отправка POST запроса на эндпоинт /api/auth/login c некорректным email")
    public void loginIncorrectEmailUnauthorizedTest() {
        loginRequest.setEmail("incorrect@email.com");

        FailureResponse loginUnauthorizedResponse = Requests.postLogin(loginRequest)
                .then()
                .statusCode(SC_UNAUTHORIZED)
                .extract()
                .as(FailureResponse.class);
        assertFalse("Вернулось значение true в поле 'success'", loginUnauthorizedResponse.isSuccess());
        assertEquals("Текст поля message в ответе не соответствует ожидаемому", LOGIN_UNAUTHORIZED, loginUnauthorizedResponse.getMessage());
    }

    @After
    @Step("Удаление созданного пользователя")
    public void deleteUser() {
        Requests.deleteUser(registerSuccessResponse.getAccessToken());
    }
}
