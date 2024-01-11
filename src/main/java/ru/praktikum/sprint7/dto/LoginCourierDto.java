package ru.praktikum.sprint7.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LoginCourierDto {
    private String login;
    private String password;

}
