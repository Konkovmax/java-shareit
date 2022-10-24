package ru.practicum.shareit.user;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final InMemoryUserStorage userStorage;

    public UserService(InMemoryUserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public UserDto create(UserDto user) {
        return UserMapper.toUserDto(userStorage.create(UserMapper.toUser(user)));
    }

    public List<UserDto> getAll() {
        return userStorage.getAll().stream()
                .map(object -> UserMapper.toUserDto(object))
                .collect(Collectors.toList());
    }

    public UserDto update(int id, UserDto user) {
        return UserMapper.toUserDto(userStorage.update(id, UserMapper.toUser(user)));
    }

    public void delete(int userId) {
        userStorage.delete(userId);
    }

    public UserDto getUser(int userId) {
        return UserMapper.toUserDto(userStorage.getUser(userId));
    }
}
