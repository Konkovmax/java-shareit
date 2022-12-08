package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.Valid;
import java.util.List;


@Controller
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserController {
    private final UserClient userService;


    @PostMapping
    public ResponseEntity<Object> create(@RequestBody @Valid UserDto user) {
        return userService.create(user);
    }

    @GetMapping
    public ResponseEntity<Object> getAll() {
        return userService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> get(@PathVariable("id") Integer userId) {
        return userService.getUser(userId);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable int id,
                          @RequestBody UserDto user) {
        return userService.update(id, user);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Object> delete(@PathVariable("id") Integer userId) {
        return userService.delete(userId);
    }

}
