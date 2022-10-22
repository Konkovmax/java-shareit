package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@NoArgsConstructor
@Data
public class UserDto {
    private int id;
    private String name;

    @Email
    @NotEmpty
    private String email;

    public UserDto(String name, String email) {
    }
}
