package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingIncomeDto;

public class BookingMapper {
    public static BookingDto toBookingDto(Booking booking) {
        return new BookingDto(
                booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                booking.getItem(),
                booking.getBooker(),
                booking.getStatus()
        );
    }

    public static BookingDateDto toBookingDateDto(Booking booking) {
        return new BookingDateDto(
                booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                booking.getItem(),
                booking.getBooker().getId(),
                booking.getStatus()
        );
    }

    public static Booking toBooking(BookingDto booking) {
        return new Booking(
                booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                booking.getItem(),
                booking.getBooker(),
                booking.getStatus()
        );
    }

    public static Booking incomeToBooking(BookingIncomeDto booking) {
        Booking newBooking = new Booking();
        newBooking.setStart(booking.getStart());
        newBooking.setEnd(booking.getEnd());
        return newBooking;
    }
}
