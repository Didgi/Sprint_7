package ru.praktikum.sprint7.commonSteps;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.response.Response;
import ru.praktikum.sprint7.CreateCourierTests;
import ru.praktikum.sprint7.DeleteCourierTests;
import ru.praktikum.sprint7.LoginCourierTests;
import ru.praktikum.sprint7.dto.CourierDto;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

@Getter
@Setter
public class CommonSteps {
    static List<String> couriersData = new ArrayList<>();
    static LoginCourierTests loginCourierTests = new LoginCourierTests();
    static CreateCourierTests createCourierTests = new CreateCourierTests();
    static DeleteCourierTests deleteCourierTests = new DeleteCourierTests();
    static final int timeoutValue = 5000;

    @Step
    public static void checkStatusCode(Response response, int code) {
        response.then().statusCode(code);
    }

    @Step
    public static void checkResponseBody(boolean isTestPositive, Response response, String detailedError, Integer code) {
        if (isTestPositive) {
            response.then().assertThat().body("ok", is(true));
        } else {
            response.then().assertThat().body("message", equalTo(detailedError)).and().body("code", equalTo(code));
        }
    }

    @Step
    protected static void clearTestCouriersData() {
        for (String dataToLogin : couriersData) {
            String login = dataToLogin.split(" ")[0];
            String password = dataToLogin.split(" ")[1];
            int idCourier = loginCourierTests.checkResponseBodyAndGetIdCourier(loginCourierTests.loginCourier(login, password));
            deleteCourierTests.deleteCourier(idCourier);
        }
        couriersData.clear();
    }

    @Step
    public static void addCouriersDataToClear(String login, String password) {
        couriersData.add(login + " " + password);
    }

    @Step
    public static void addHttpTimeout() {
        RestAssured.config = RestAssuredConfig.config()
                .httpClient(HttpClientConfig.httpClientConfig()
                        .setParam("http.socket.timeout", timeoutValue)
                        .setParam("http.connection.timeout", timeoutValue));
    }

    @Step
    public static int prepareCourier(String randomLogin, String randomPassword) {
        createCourierTests = new CreateCourierTests();
        createCourierTests.createCourier(new CourierDto(randomLogin, randomPassword, null));
        loginCourierTests = new LoginCourierTests();
        Response responseLogin = loginCourierTests.loginCourier(randomLogin, randomPassword);
        return loginCourierTests.checkResponseBodyAndGetIdCourier(responseLogin);
    }
}
