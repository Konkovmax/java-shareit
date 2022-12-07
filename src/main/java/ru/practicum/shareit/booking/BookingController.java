package ru.practicum.shareit.booking;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingIncomeDto;
import ru.practicum.shareit.exception.BadRequestException;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingServiceImpl bookingService;

    public BookingController(BookingServiceImpl bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public BookingDto create(@Valid @RequestBody BookingIncomeDto booking,
                             @RequestHeader(value = "X-Sharer-User-Id") int userId) {
        return bookingService.create(booking, userId);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto update(@PathVariable int bookingId,
                             @RequestHeader(value = "X-Sharer-User-Id") int userId,
                             @RequestParam(value = "approved") boolean approved) {
        return bookingService.update(bookingId, userId, approved);
    }

    @GetMapping
    public List<BookingDto> getAll(@RequestHeader(value = "X-Sharer-User-Id") int userId,
                                   @RequestParam(value = "from", defaultValue = "0", required = false) int from,
                                   @RequestParam(value = "size", defaultValue = "10", required = false) int size,
                                   @RequestParam(value = "state", defaultValue = "ALL", required = false) String state) {
        if (from < 0 || size < 1) {
            throw new BadRequestException("Incorrect pagination parameters");
        }
        return bookingService.getAllForUser(userId,
                PageRequest.of((size > from) ? 0 : from / size, size, Sort.by("start").descending()), state);
    }

    @GetMapping("/owner")
    public List<BookingDto> getAllOwners(@RequestHeader(value = "X-Sharer-User-Id") int userId,
                                         @RequestParam(value = "from", defaultValue = "0", required = false) int from,
                                         @RequestParam(value = "size", defaultValue = "10", required = false) int size,
                                         @RequestParam(value = "state", defaultValue = "ALL", required = false) String state) {
        return bookingService.getAllForOwner(userId, from, size, state);
    }

    @GetMapping("/{bookingId}")
    public BookingDto get(@PathVariable("bookingId") Integer bookingId,
                          @RequestHeader(value = "X-Sharer-User-Id") int userId) {
        return bookingService.getBooking(bookingId, userId);
    }
}
