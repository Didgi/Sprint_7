package ru.praktikum.sprint7.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetOrdersListDto {
    private Integer courierId;
    private String[] nearestStation;
    private Integer limit;
    private Integer page;

}
