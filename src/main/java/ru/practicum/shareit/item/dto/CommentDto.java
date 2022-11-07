package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class CommentDto {
    private int id;
    @NotEmpty
    private String text;
    //private Item item;
    private String authorName;
//    private User author;
    private LocalDateTime created;
}
