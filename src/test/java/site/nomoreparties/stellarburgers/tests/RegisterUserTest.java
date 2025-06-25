package site.nomoreparties.stellarburgers.tests;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import site.nomoreparties.stellarburgers.models.requests.RegisterRequest;
import site.nomoreparties.stellarburgers.models.responses.FailureResponse;
import site.nomoreparties.stellarburgers.models.responses.UserSuccessResponse;
import site.nomoreparties.stellarburgers.utils.Requests;

import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.*;
import static site.nomoreparties.stellarburgers.utils.Constants.REGISTER_REQUIRED_FIELDS;
import static site.nomoreparties.stellarburgers.utils.Constants.REGISTER_USER_ALREADY_EXISTS;

public class RegisterUserTest extends BaseTest {

    String email;
    String password;
    String name;

    RegisterRequest registerRequest;
    UserSuccessResponse registerSuccessResponse;

    @Before
    @Step("Инициализация параметров: email, password, name, registerRequest")
    public void initParams() {
        email = RandomStringUtils.randomAlphabetic(5) + "@ya.ru";
        password = RandomStringUtils.randomAlphanumeric(9);
        name = RandomStringUtils.randomAlphabetic(7);
        registerRequest = new RegisterRequest(email, password, name);
    }

    @Test
    @DisplayName("Успешное создание уникального пользователя")
    @Description("Отправка POST запроса на эндпоинт /api/auth/register со всеми обязательными параметрами")
    public void registerUniqueUserSuccessTest() {
        registerSuccessResponse = Requests.postRegister(registerRequest)
                .then()
                .statusCode(SC_OK)
                .extract()
                .as(UserSuccessResponse.class);
        assertTrue("Вернулось значение false в поле 'success'", registerSuccessResponse.isSuccess());
    }

    @Test
    @DisplayName("Создание уникального пользователя. Проверка, что значения полей name и email в ответе соответствуют значениям полей в запросе")
    @Description("Отправка POST запроса на эндпоинт /api/auth/register со всеми обязательными параметрами")
    public void registerUniqueUserReturnEqualsCredentialsTest() {
        registerSuccessResponse = Requests.postRegister(registerRequest)
                .then()
                .extract()
                .as(UserSuccessResponse.class);
        assertEquals("Значение поля name в ответе отличается от переданного в запросе", name, registerSuccessResponse.getUser().getName());
        assertEquals("Значение поля email в ответе отличается от переданного в запросе", email.toLowerCase(), registerSuccessResponse.getUser().getEmail());
    }

    @Test
    @DisplayName("При создании пользователя со значениями email, который был зарегестрирован ранее, в ответе возвращается код 403 Forbidden с сообщением 'User already exists'")
    @Description("Отправка POST запроса на эндпоинт /api/auth/register со значением email, ранее зарегистрированного пользователя")
    public void registerUserSameEmailForbiddenTest() {
        registerSuccessResponse = Requests.postRegister(registerRequest).as(UserSuccessResponse.class);
        registerRequest.setEmail("test@ya.ru");
        registerRequest.setName("Test");
        FailureResponse registerForbiddenResponse = Requests.postRegister(registerRequest)
                .then()
                .statusCode(SC_FORBIDDEN)
                .extract()
                .as(FailureResponse.class);
        assertFalse("Вернулось значение true в поле 'success'", registerForbiddenResponse.isSuccess());
        assertEquals("Текст поля message в ответе не соответствует ожидаемому", REGISTER_USER_ALREADY_EXISTS, registerForbiddenResponse.getMessage());
    }

    @Test
    @DisplayName("При создании пользователя с незаполненным значением email, в ответе возвращается код 403 Forbidden с сообщением 'Email, password and name are required fields'")
    @Description("Отправка POST запроса на эндпоинт /api/auth/register незаполненным значением email")
    public void registerUserEmptyEmailForbiddenTest() {
        registerRequest.setEmail("");
        FailureResponse registerForbiddenResponse = Requests.postRegister(registerRequest)
                .then()
                .statusCode(SC_FORBIDDEN)
                .extract()
                .as(FailureResponse.class);
        assertFalse("Вернулось значение true в поле 'success'", registerForbiddenResponse.isSuccess());
        assertEquals("Текст поля message в ответе не соответствует ожидаемому", REGISTER_REQUIRED_FIELDS, registerForbiddenResponse.getMessage());
    }

    @Test
    @DisplayName("При создании пользователя с незаполненным значением password, в ответе возвращается код 403 Forbidden с сообщением 'Email, password and name are required fields'")
    @Description("Отправка POST запроса на эндпоинт /api/auth/register незаполненным значением password")
    public void registerUserEmptyPasswordForbiddenTest() {
        registerRequest.setPassword("");
        FailureResponse registerForbiddenResponse = Requests.postRegister(registerRequest)
                .then()
                .statusCode(SC_FORBIDDEN)
                .extract()
                .as(FailureResponse.class);
        assertFalse("Вернулось значение true в поле 'success'", registerForbiddenResponse.isSuccess());
        assertEquals("Текст поля message в ответе не соответствует ожидаемому", REGISTER_REQUIRED_FIELDS, registerForbiddenResponse.getMessage());
    }

    @Test
    @DisplayName("При создании пользователя с незаполненным значением name, в ответе возвращается код 403 Forbidden с сообщением 'Email, password and name are required fields'")
    @Description("Отправка POST запроса на эндпоинт /api/auth/register незаполненным значением name")
    public void registerUserEmptyNameForbiddenTest() {
        registerRequest.setName("");
        FailureResponse registerForbiddenResponse = Requests.postRegister(registerRequest)
                .then()
                .statusCode(SC_FORBIDDEN)
                .extract()
                .as(FailureResponse.class);
        assertFalse("Вернулось значение true в поле 'success'", registerForbiddenResponse.isSuccess());
        assertEquals("Текст поля message в ответе не соответствует ожидаемому", REGISTER_REQUIRED_FIELDS, registerForbiddenResponse.getMessage());
    }

    @After
    @Step("Удаление созданного пользователя")
    public void deleteUser() {
        if(registerSuccessResponse != null) {
            Requests.deleteUser(registerSuccessResponse.getAccessToken());
        }
    }

}
