package ru.practicum.shareit;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingServiceImpl;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.dto.BookingDateDto;
import ru.practicum.shareit.booking.dto.BookingIncomeDto;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class BookingServiceTests {
    private final BookingServiceImpl bookingService;

    @Test
    public void testBookingValidationPastStart() {
        BadRequestException ex = assertThrows(
                BadRequestException.class,
                () -> {
                    BookingIncomeDto booking = new BookingIncomeDto(LocalDateTime.now().minusDays(3),
                            LocalDateTime.now().plusDays(10), 1);
                    bookingService.throwIfNotValid(booking);
                });
        Assertions.assertEquals("Booking start can't be in the past", ex.getMessage());
    }

    @Test
    public void testBookingValidationPastEnd() {
        BadRequestException ex = assertThrows(
                BadRequestException.class,
                () -> {
                    BookingIncomeDto booking = new BookingIncomeDto(LocalDateTime.now(),
                            LocalDateTime.now().minusDays(10), 1);
                    bookingService.throwIfNotValid(booking);
                });
        Assertions.assertEquals("Booking ending can't be in the past", ex.getMessage());
    }

    @Test
    public void testIncorrectPagination() {
        BadRequestException ex = assertThrows(
                BadRequestException.class,
                () -> bookingService.getAllForUser(1, -2, -1, "PAST"));
        Assertions.assertEquals("Incorrect pagination parameters", ex.getMessage());
    }

    @Test
    public void testBookingValidationEndBeforeStart() {
        BadRequestException ex = assertThrows(
                BadRequestException.class,
                () -> {
                    BookingIncomeDto booking = new BookingIncomeDto(LocalDateTime.now().plusDays(10),
                            LocalDateTime.now().plusDays(3), 1);
                    bookingService.throwIfNotValid(booking);
                });
        Assertions.assertEquals("Booking start can't be after end", ex.getMessage());
    }

    @Test
    public void bookingFutureStatusTest() {
        List<Booking> newBooking = Arrays.asList(
                new Booking(1, LocalDateTime.now(), LocalDateTime.now().minusDays(2),
                        null, null, Status.WAITING),
                new Booking(2, LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(3),
                        null, null, Status.WAITING));
        List<Booking> filterBooking = newBooking.stream()
                .filter(bookingService.bookingStatus("FUTURE"))
                .collect(Collectors.toList());
        Assertions.assertEquals(filterBooking.get(0).getId(), 2);
    }

    @Test
    public void bookingCurrentStatusTest() {
        List<Booking> newBooking = Arrays.asList(
                new Booking(1, LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2),
                        null, null, Status.WAITING),
                new Booking(2, LocalDateTime.now().minusDays(1), LocalDateTime.now().plusDays(3),
                        null, null, Status.WAITING));
        List<Booking> filterBooking = newBooking.stream()
                .filter(bookingService.bookingStatus("CURRENT"))
                .collect(Collectors.toList());
        Assertions.assertEquals(filterBooking.get(0).getId(), 2);
    }

    @Test
    public void bookingWaitingStatusTest() {
        List<Booking> newBooking = Arrays.asList(
                new Booking(1, LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2),
                        null, null, Status.REJECTED),
                new Booking(2, LocalDateTime.now().minusDays(1), LocalDateTime.now().plusDays(3),
                        null, null, Status.WAITING));
        List<Booking> filterBooking = newBooking.stream()
                .filter(bookingService.bookingStatus("WAITING"))
                .collect(Collectors.toList());
        Assertions.assertEquals(filterBooking.get(0).getId(), 2);
    }

    @Test
    public void bookingRejectedStatusTest() {
        List<Booking> newBooking = Arrays.asList(
                new Booking(1, LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2),
                        null, null, Status.APPROVED),
                new Booking(2, LocalDateTime.now().minusDays(1), LocalDateTime.now().plusDays(3),
                        null, null, Status.REJECTED));
        List<Booking> filterBooking = newBooking.stream()
                .filter(bookingService.bookingStatus("REJECTED"))
                .collect(Collectors.toList());
        Assertions.assertEquals(filterBooking.get(0).getId(), 2);
    }

    @Test
    public void bookingPastStatusTest() {
        List<Booking> newBooking = Arrays.asList(
                new Booking(1, LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2),
                        null, null, Status.APPROVED),
                new Booking(2, LocalDateTime.now().minusDays(5), LocalDateTime.now().minusDays(3),
                        null, null, Status.APPROVED));
        List<Booking> filterBooking = newBooking.stream()
                .filter(bookingService.bookingStatus("PAST"))
                .collect(Collectors.toList());
        Assertions.assertEquals(filterBooking.get(0).getId(), 2);
    }

    @Test
    public void bookingStatusTestFailed() {
        BadRequestException ex = assertThrows(
                BadRequestException.class,
                () -> bookingService.bookingStatus("Error"));
        Assertions.assertEquals("Unknown state: UNSUPPORTED_STATUS", ex.getMessage());
    }

    @Test
    public void bookingMapperTest() {
        User newUser = new User(1, "Name", "email@email.com");
        Booking booking = new Booking(1, null, null,
                null, newUser, Status.WAITING);
        BookingDateDto expectedBooking = new BookingDateDto(1, null, null,
                null, 1, Status.WAITING);
        Assertions.assertEquals(expectedBooking, BookingMapper.toBookingDateDto(booking));
    }
}
