package ru.praktikum.sprint7;

import io.qameta.allure.Description;
import io.qameta.allure.Issue;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.praktikum.sprint7.commonSteps.CommonSteps;
import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.*;
import static ru.praktikum.sprint7.configs.Config.*;

public class DeleteCourierTests extends CommonSteps {
    private static final String randomLogin = RandomStringUtils.randomAlphabetic(7);
    private static final String randomPassword = RandomStringUtils.randomAlphabetic(7);
    private static int idCourier;

    @BeforeClass
    public static void setUp() {
        RestAssured.baseURI = BASE_URI;
        idCourier = prepareCourier(randomLogin, randomPassword);
    }
    @Step
    public Response deleteCourier(Integer idCourier) {
        if (idCourier != null && idCourier > 0) {
            return given()
                    .when()
                    .delete(DELETE_COURIER_HANDLE + idCourier);
        } else {
            return given()
                    .when()
                    .delete(DELETE_COURIER_HANDLE);
        }
    }

    @Test
    @DisplayName("Удаление существующего курьера")
    @Description("В этом тесте проверяется удаление курьера с валидными данными")
    public void deleteCourierExistedIdShowsOk() {
        Response response = deleteCourier(idCourier);
        checkStatusCode(response, SC_OK);
        checkResponseBody(true, response, null, null);
    }

    @Test
    @DisplayName("Удаление не существующего курьера")
    @Description("В этом тесте проверяется, что при попытке удаления несуществующего курьера вовзращается 404 код и проверяется тело ответа ")
    @Issue("BUG-3")
    public void deleteCourierNotExistedIdShowsError() {
        deleteCourier(idCourier);
        Response responseNotExisted = deleteCourier(idCourier);
        checkStatusCode(responseNotExisted, SC_NOT_FOUND);
        checkResponseBody(false, responseNotExisted, DELETE_DETAILED_ERROR_TEXT_404, SC_NOT_FOUND);
    }

    @Test
    @DisplayName("Удаление без передачи id курьера")
    @Description("В этом тесте проверяется, что при попытке удаление без передачи id курьера вовзращается 400 код и проверяется тело ответа ")
    @Issue("BUG-4")
    public void deleteCourierWithoutIdShowsError() {
        Response response = deleteCourier(null);
        checkStatusCode(response, SC_BAD_REQUEST);
        checkResponseBody(false, response, DELETE_DETAILED_ERROR_TEXT_400, SC_BAD_REQUEST);
    }


}
