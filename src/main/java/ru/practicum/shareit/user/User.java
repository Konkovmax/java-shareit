package ru.practicum.shareit.user;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Data
public class User {
    private int id;
    private String name;

    @Email
    @NotEmpty
    private String email;
}
