package ru.praktikum.sprint7;

import io.qameta.allure.Description;
import io.qameta.allure.Issue;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import lombok.Getter;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.praktikum.sprint7.commonSteps.CommonSteps;
import ru.praktikum.sprint7.dto.*;
import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.notNullValue;
import static ru.praktikum.sprint7.configs.Config.*;

public class LoginCourierTests extends CommonSteps {
    private LoginCourierDto loginCourierDto;
    private static final String randomLogin = RandomStringUtils.randomAlphabetic(7);
    private static final String randomPassword = RandomStringUtils.randomAlphabetic(7);
    @Getter
    private static int idCourier;

    @BeforeClass
    public static void setUp() {
        RestAssured.baseURI = BASE_URI;
        prepareCourier(randomLogin, randomPassword);
        addCouriersDataToClear(randomLogin, randomPassword);
        addHttpTimeout();
    }


    @Step
    public Response loginCourier(String login, String password) {
        loginCourierDto = new LoginCourierDto(login, password);
        return given()
                .contentType(ContentType.JSON)
                .and()
                .body(loginCourierDto)
                .when()
                .post(LOGIN_COURIER_HANDLE);

    }

    @Step
    public int checkResponseBodyAndGetIdCourier(Response response) {
        return idCourier = response.then().assertThat().body("id", notNullValue()).and().extract().path("id");
    }

    @Test
    @DisplayName("Логин под существующим курьером")
    @Description("В этом тесте проверяется получение id курьера при выполнении логина с валидными данными")
    public void loginCourierPositiveAllFieldsShowsOk() {
        Response response = loginCourier(randomLogin, randomPassword);
        checkStatusCode(response, SC_OK);
        checkResponseBodyAndGetIdCourier(response);
    }

    @Test
    @DisplayName("Логин под несуществующим курьером")
    @Description("В этом тесте проверяется, что при выполнении логина под несуществующим курьером возвращается 404 код и проверяется тело ответа")
    public void loginCourierNegativeNotExistedLoginShowsError() {
        Response response = loginCourier("notExistedCourier", randomPassword);
        checkStatusCode(response, SC_NOT_FOUND);
        checkResponseBody(false, response, LOGIN_DETAILED_ERROR_TEXT_404, SC_NOT_FOUND);
    }

    @Test
    @DisplayName("Логин с указанием пароля в другом регистре")
    @Description("В этом тесте проверяется, что при выполнении логина и передачи пароля в другом регистре возвращается 404 код и проверяется тело ответа")
    public void loginCourierNegativeDifferentPasswordRegisterShowsError() {
        Response response = loginCourier(randomLogin, randomPassword.toUpperCase());
        checkStatusCode(response, SC_NOT_FOUND);
        checkResponseBody(false, response, LOGIN_DETAILED_ERROR_TEXT_404, SC_NOT_FOUND);
    }

    @Test
    @DisplayName("Логин с передачей пустого пароля")
    @Description("В этом тесте проверяется, что при выполнении логина с пустым паролем возвращается 400 код и проверяется тело ответа")
    public void loginCourierNegativeEmptyPasswordShowsError() {
        Response response = loginCourier(randomLogin, "");
        checkStatusCode(response, SC_BAD_REQUEST);
        checkResponseBody(false, response, LOGIN_DETAILED_ERROR_TEXT_400, SC_BAD_REQUEST);
    }

    @Test
    @DisplayName("Логин без указания пароля")
    @Description("В этом тесте проверяется, что при выполнении логина без пароля возвращается 400 код и проверяется тело ответа")
    @Issue("BUG-2")
    public void loginCourierNegativeWithoutPasswordShowsError() {
        Response response = loginCourier(randomLogin, null);
        checkStatusCode(response, SC_BAD_REQUEST);
        checkResponseBody(false, response, LOGIN_DETAILED_ERROR_TEXT_400, SC_BAD_REQUEST);
    }

    @Test
    @DisplayName("Логин без указания логина")
    @Description("В этом тесте проверяется, что при выполнении логина без логина возвращается 400 код и проверяется тело ответа")
    public void loginCourierNegativeWithoutLoginShowsError() {
        Response response = loginCourier(null, randomPassword);
        checkStatusCode(response, SC_BAD_REQUEST);
        checkResponseBody(false, response, LOGIN_DETAILED_ERROR_TEXT_400, SC_BAD_REQUEST);
    }

    @AfterClass
    public static void tearDown() {
        clearTestCouriersData();
    }

}
