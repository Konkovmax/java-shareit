package ru.practicum.shareit.user;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class UserService {
    private final InMemoryUserStorage userStorage;
  //  private Map<Integer, User> users;

    public UserService(InMemoryUserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public UserDto create(UserDto user){
        return userStorage.create(user);
    }

    public List<UserDto> getAll(){
        return userStorage.getAll();
    }

    public UserDto update(int id,UserDto user){
        return userStorage.update(id, user);
    }
    public void delete(int userId){
        userStorage.delete(userId);
    }

    public UserDto getUser(int userId){
        return userStorage.getUser(userId);
    }
}
