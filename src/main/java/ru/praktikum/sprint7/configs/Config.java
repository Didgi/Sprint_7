package ru.praktikum.sprint7.configs;

public class Config {
    public static final String BASE_URI = "https://qa-scooter.praktikum-services.ru";
    public static final String CREATE_COURIER_HANDLE = "/api/v1/courier/";
    public static final String LOGIN_COURIER_HANDLE = "/api/v1/courier/login";
    public static final String DELETE_COURIER_HANDLE = "/api/v1/courier/";
    public static final String CREATE_ORDER_HANDLE = "/api/v1/orders/";
    public static final String CANCEL_ORDER_HANDLE = "/api/v1/orders/cancel";
    public static final String GET_ORDERS_HANDLE = "/api/v1/orders";
    public static final String CREATE_DETAILED_ERROR_TEXT_404 = "Недостаточно данных для создания учетной записи";
    public static final String CREATE_DETAILED_ERROR_TEXT_409 = "Этот логин уже используется";
    public static final String LOGIN_DETAILED_ERROR_TEXT_404 = "Учетная запись не найдена";
    public static final String LOGIN_DETAILED_ERROR_TEXT_400 = "Недостаточно данных для входа";
    public static final String DELETE_DETAILED_ERROR_TEXT_400 = "Недостаточно данных для удаления курьера";
    public static final String DELETE_DETAILED_ERROR_TEXT_404 = "Курьера с таким id нет";

}
