package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;

@AllArgsConstructor
@Data
public class BookingDateDto {
    private int id;
    private LocalDateTime start;
    private LocalDateTime end;
    private Item item;
    private int bookerId;
    private Status status;
}
