package ru.practicum.shareit.request;

import lombok.Data;
import org.apache.catalina.User;

import java.time.LocalDate;

@Data
public class ItemRequest {
    private int id;
    private String description;
    private User requestor;
    private LocalDate created;
}
