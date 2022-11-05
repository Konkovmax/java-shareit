package ru.practicum.shareit.booking;

import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingIncomeDto;

import javax.validation.Valid;

/**
 * TODO Sprint add-bookings.
 */
@RestController
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingServiceImpl bookingService;

    public BookingController(BookingServiceImpl bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public BookingDto create(@Valid @RequestBody BookingIncomeDto booking, @RequestHeader(value = "X-Sharer-User-Id",
            required = true) int userId) {
        return bookingService.create(booking, userId);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto update(@PathVariable int bookingId,
                             @RequestHeader(value = "X-Sharer-User-Id", required = true) int userId,
                             @RequestParam(value = "approved", required = true) Boolean approved) {
        return bookingService.update(bookingId, userId, approved);
    }

//    @GetMapping
//    public List<BookingDto> getAll() {
//        return bookingService.getAll();
//    }
//
//    @GetMapping("/{id}")
//    public BookingDto get(@PathVariable("id") Integer bookingId) {
//        return bookingService.getBooking(bookingId);
//    }
}
