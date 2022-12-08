package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class CommentDto {
    private int id;

    @NotEmpty
    private String text;
    private String authorName;
    private LocalDateTime created;
}
