package ru.practicum.shareit.request.dto;

import org.apache.catalina.User;

import java.time.LocalDate;

public class ItemRequestDto {
    private int id;
    private String description;
    private User requestor;
    private LocalDate created;
}
