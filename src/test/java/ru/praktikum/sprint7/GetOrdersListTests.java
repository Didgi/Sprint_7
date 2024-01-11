package ru.praktikum.sprint7;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import ru.praktikum.sprint7.commonSteps.CommonSteps;
import ru.praktikum.sprint7.dto.GetOrdersListDto;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.CoreMatchers.notNullValue;
import static ru.praktikum.sprint7.configs.Config.*;

public class GetOrdersListTests extends CommonSteps {
    private GetOrdersListDto getOrdersListDto;

    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URI;
    }

    @Step
    public Response getOrdersList(GetOrdersListDto getOrdersListDto) {
        return given()
                .queryParam("courierId", getOrdersListDto.getCourierId())
                .queryParam("nearestStation", (Object[]) getOrdersListDto.getNearestStation())
                .queryParam("limit", getOrdersListDto.getLimit())
                .queryParam("page", getOrdersListDto.getPage())
                .when()
                .get(GET_ORDERS_HANDLE);
    }

    @Step
    public void checkResponseBodyOrderList(Response response) {
        response.then().assertThat().body("orders", notNullValue());
    }

    @Test
    @DisplayName("Получение списка заказов")
    @Description("В этом тесте проверяется, что будет получен не пустой список заказов")
    public void getOrdersListWithoutRequiredFieldsShowsOk() {
        getOrdersListDto = new GetOrdersListDto(null, null, null, null); //сделал специально с query параметрами, как задел на будущее
        Response response = getOrdersList(getOrdersListDto);
        checkStatusCode(response, SC_OK);
        checkResponseBodyOrderList(response);

    }

}
