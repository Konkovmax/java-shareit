package ru.practicum.shareit.user;

import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping(path = "/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public UserDto create(@Valid @RequestBody User user) {
        return userService.create(user);
    }

    @GetMapping
    public List<UserDto> getAll() {
        return userService.getAll();
    }

    @GetMapping("/{id}")
    public UserDto get(@PathVariable("id") Integer userId) {
        return userService.getUser(userId);
    }

    @PatchMapping("/{id}")
    public UserDto update(@PathVariable int id, //@Valid
    @RequestBody User user) {
        return userService.update(id,user);
    }

    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable("id") Integer userId) {
        userService.delete(userId);
    }

}
