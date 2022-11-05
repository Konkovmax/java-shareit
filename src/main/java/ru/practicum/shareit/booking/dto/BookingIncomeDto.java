package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;

@AllArgsConstructor
@Data
public class BookingIncomeDto {
   // private int id;
    private LocalDateTime start;
    private LocalDateTime end;
    private int itemId;
//    private User booker;
//    private Status status;
}
