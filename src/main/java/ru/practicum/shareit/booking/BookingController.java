package ru.practicum.shareit.booking;

import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.UserDto;
import ru.practicum.shareit.user.UserService;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-bookings.
 */
@RestController
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingService bookingService;

    public UserController(UserService userService) {
        this.bookingService = userService;
    }

    @PostMapping
    public UserDto create(@Valid @RequestBody UserDto user) {
        return bookingService.create(user);
    }

    @GetMapping
    public List<UserDto> getAll() {
        return bookingService.getAll();
    }

    @GetMapping("/{id}")
    public UserDto get(@PathVariable("id") Integer userId) {
        return bookingService.getUser(userId);
    }

    @PatchMapping("/{id}")
    public UserDto update(@PathVariable int id, //@Valid
                          @RequestBody UserDto user) {
        return bookingService.update(id, user);
    }

    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable("id") Integer userId) {
        bookingService.delete(userId);
    }
}
