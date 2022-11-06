package ru.practicum.shareit.booking;

import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingIncomeDto;

import javax.validation.Valid;
import java.util.List;

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
                             @RequestParam(value = "approved", required = true) boolean approved) {
        return bookingService.update(bookingId, userId, approved);
    }

    @GetMapping
    public List<BookingDto> getAll(@RequestHeader(value = "X-Sharer-User-Id", required = true) int userId,
                                   @RequestParam(value = "state", defaultValue = "ALL", required = false) String state){
        return bookingService.getAll(userId, state, false);
    }

    @GetMapping("/owner")
    public List<BookingDto> getAllOwners(@RequestHeader(value = "X-Sharer-User-Id", required = true) int userId,
                                   @RequestParam(value = "state", defaultValue = "ALL", required = false) String state){
        return bookingService.getAll(userId, state, true);
    }

    @GetMapping("/{bookingId}")
    public BookingDto get(@PathVariable("bookingId") Integer bookingId,
                          @RequestHeader(value = "X-Sharer-User-Id") int userId) {
        return bookingService.getBooking(bookingId, userId);
    }
}
