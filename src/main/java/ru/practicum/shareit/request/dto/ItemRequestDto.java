package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.user.User;


import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;

@AllArgsConstructor
@Data
public class ItemRequestDto {
    private int id;

    @NotEmpty
    private String description;
    private User requester;
    private LocalDate created;
}
