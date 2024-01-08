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
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.praktikum.sprint7.commonSteps.CommonSteps;
import ru.praktikum.sprint7.dto.*;

import static ru.praktikum.sprint7.configs.Config.*;
import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.*;

@Getter
public class CreateCourierTests extends CommonSteps {
    private final String randomLogin = RandomStringUtils.randomAlphabetic(7);
    private final String randomPassword = RandomStringUtils.randomAlphabetic(7);
    private final String randomFirstName = RandomStringUtils.randomAlphabetic(7);
    private CourierDto courierDto;


    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URI;
        courierDto = new CourierDto();
    }

    @Step
    public Response createCourier(CourierDto courierDto) {
        return given()
                .contentType(ContentType.JSON)
                .and()
                .body(courierDto)
                .when()
                .post(CREATE_COURIER_HANDLE);
    }

    @Test
    @DisplayName("Создание курьера c всеми полями") //cкорее всего данный тест тут не нужен. Унести в другой тест?
    @Description("В этом тесте проверяется, что при создании курьера возвращается 201 код и ok в теле ответа")
    public void createCourierPositiveAllFieldsShowsOk() {
        Response response = createCourier(new CourierDto(randomLogin, randomPassword, randomFirstName));
        addCouriersDataToClear(randomLogin, randomPassword);
        checkStatusCode(response, SC_CREATED);
        checkResponseBody(true, response, null, null);
    }

    @Test
    @DisplayName("Создание курьера только с обязательными полями")
    @Description("В этом тесте проверяется, что при создании курьера только с обязательными полями (без firstName) возвращается 201 код и ok в теле ответа")
    public void createCourierPositiveAllRequiredFieldsShowsOk() {
        Response response = createCourier(new CourierDto(randomLogin, randomPassword, null));
        checkStatusCode(response, SC_CREATED);
        addCouriersDataToClear(randomLogin, randomPassword);
        checkResponseBody(true, response, null, null);
    }

    @Test
    @DisplayName("Повторное создание курьера с теми же данными")
    @Description("В этом тесте проверяется, что при создании курьера, который уже существует возвращается 409 код и проверяется наличие текста ошибки в теле ответа")
    @Issue("BUG-1")
    public void createCourierNegativeDuplicateCourierShowsError() {
        createCourier(new CourierDto(randomLogin, randomPassword, null));
        Response responseDuplicate = createCourier(new CourierDto(randomLogin, randomPassword, null));
        addCouriersDataToClear(randomLogin, randomPassword);
        checkStatusCode(responseDuplicate, SC_CONFLICT);
        checkResponseBody(false, responseDuplicate, CREATE_DETAILED_ERROR_TEXT_409, SC_CONFLICT);
    }

    @Test
    @DisplayName("Создание курьера с пустым обязательным полем Login")
    @Description("В этом тесте проверяется, что при создании курьера с пустым обязательным полем Login приходит 400 код и проверяется наличие текста ошибки в теле ответа")
    public void createCourierNegativeWithEmptyLoginShowsError() {
        Response response = createCourier(new CourierDto("", randomPassword, randomFirstName));
        checkStatusCode(response, SC_BAD_REQUEST);
        checkResponseBody(false, response, CREATE_DETAILED_ERROR_TEXT_404, SC_BAD_REQUEST);
    }

    @Test
    @DisplayName("Создание курьера без обязательного поля Login")
    @Description("В этом тесте проверяется, что при создании курьера без обязательного поля Login приходит 400 код и проверяется наличие текста ошибки в теле ответа")
    public void createCourierNegativeWithoutLoginShowsError() {
        Response response = createCourier(new CourierDto(null, randomPassword, randomFirstName));
        checkStatusCode(response, SC_BAD_REQUEST);
        checkResponseBody(false, response, CREATE_DETAILED_ERROR_TEXT_404, SC_BAD_REQUEST);
    }

    @Test
    @DisplayName("Создание курьера с пустым обязательным полем Password")
    @Description("В этом тесте проверяется, что при создании курьера с пустым обязательным полем Password приходит 400 код и проверяется наличие текста ошибки в теле ответа")
    public void createCourierNegativeWithEmptyPasswordShowsError() {
        Response response = createCourier(new CourierDto(randomLogin, "", randomFirstName));
        checkStatusCode(response, SC_BAD_REQUEST);
        checkResponseBody(false, response, CREATE_DETAILED_ERROR_TEXT_404, SC_BAD_REQUEST);
    }

    @Test
    @DisplayName("Создание курьера без обязательного поля Password")
    @Description("В этом тесте проверяется, что при создании курьера без обязательного поля Password приходит 400 код и проверяется наличие текста ошибки в теле ответа")
    public void createCourierNegativeWithoutPasswordShowsError() {
        Response response = createCourier(new CourierDto(randomLogin, null, randomFirstName));
        checkStatusCode(response, SC_BAD_REQUEST);
        checkResponseBody(false, response, CREATE_DETAILED_ERROR_TEXT_404, SC_BAD_REQUEST);

    }

    @After
    public void tearDown() {
        clearTestCouriersData();
    }
}
