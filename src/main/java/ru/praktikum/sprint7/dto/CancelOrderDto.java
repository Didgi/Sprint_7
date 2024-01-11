package ru.praktikum.sprint7.dto;


public class CancelOrderDto {
    private String track;

    public CancelOrderDto(int idOrder) {
        this.track = String.valueOf(idOrder);
    }
}
