package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingIncomeDto;

import java.util.List;

public interface BookingService {

    BookingDto create(BookingIncomeDto booking, int userId);

    List<BookingDto> getAllForUser(int userId, String stateIncome);

    List<BookingDto> getAllForOwner(int userId, String stateIncome);

    BookingDto update(int bookingId, int userId, boolean approved);

    BookingDto getBooking(int bookingId, int userId);
}
