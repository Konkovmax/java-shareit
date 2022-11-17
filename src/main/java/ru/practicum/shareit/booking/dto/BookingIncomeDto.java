package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@AllArgsConstructor
@Data
public class BookingIncomeDto {
    private LocalDateTime start;
    private LocalDateTime end;
    private int itemId;
}
