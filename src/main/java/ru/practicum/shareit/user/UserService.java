package ru.practicum.shareit.user;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final InMemoryUserStorage userStorage;
  //  private Map<Integer, User> users;

    public UserService(InMemoryUserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public UserDto create(User user){
        return UserMapper.toUserDto(userStorage.create(user));
    }

    public List<UserDto> getAll(){
        return  userStorage.getAll().stream()
                .map(object -> UserMapper.toUserDto(object))
                .collect(Collectors.toList());
    }

    public UserDto update(int id,User user){
        return UserMapper.toUserDto(userStorage.update(id, user));
    }

    public void delete(int userId){
        userStorage.delete(userId);
    }

    public UserDto getUser(int userId){
        return UserMapper.toUserDto(userStorage.getUser(userId));
    }
}
