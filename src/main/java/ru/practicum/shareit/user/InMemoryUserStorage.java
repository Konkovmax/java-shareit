package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.NotFoundException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class InMemoryUserStorage {
    public Map<Integer, User> users = new HashMap<>();
    private int id = 1;

    public User create(User user) {
        throwIfNotValid(user);
        user.setId(id);
        generateId();
        users.put(user.getId(), user);
        return user;
    }

    public void delete(int userId) {
        if (users.containsKey(userId)) {
            users.remove(userId);
            log.info("User deleted");
        } else {
            log.warn("user not found");
            throw new NotFoundException(String.format(
                    "User with id: %s not found",
                    userId));
        }
    }

    public boolean throwIfNotValid(User newUser) throws BadRequestException {
        for (User user:users.values()) {
            if (user.getEmail().equals(newUser.getEmail())) {
                log.error("Duplicated Email");
                throw new ConflictException("Email can't duplicated");
            }
        }
//        if (user.getLogin().isEmpty()) {
//            log.error("login can't be empty");
//            throw new BadRequestException("login can't be empty");
//        }
//        if (user.getBirthday().isAfter(LocalDate.now())) {
//            log.error("Birthday can't be in the Future");
//            throw new BadRequestException("Birthday can't be in the Future");
//        }
//        if (!user.getEmail().contains("@")) {
//            log.error("email should contain @");
//            throw new BadRequestException("email should contain @");
//        }
//        if (user.getLogin().contains(" ")) {
//            log.error("login can't contain spaces");
//            throw new BadRequestException("login can't contain spaces");
//        }
        return true;

    }
    public User update(int id, User user) {
        User updateUser = new User();
        if (users.containsKey(id)) {
            for (User userCheck:users.values()) {
                if (userCheck.getEmail().equals(user.getEmail())&userCheck.getId()!= user.getId()) {
                    log.error("Duplicated Email");
                    throw new ConflictException("Email can't duplicated");
                }
            }
            updateUser=users.get(id);
            updateUser.setId(id);
            if(user.getName()!=null){
                updateUser.setName(user.getName());
            }
            if(user.getEmail()!=null){
                updateUser.setEmail(user.getEmail());
            }
            users.put(id, updateUser);
            log.info("User updated");
        } else {
//todo change to ljambda
//todo            explain empty service or move to service
            log.warn("user not found");
            throw new NotFoundException(String.format(
                    "User with id: %s not found",
                    id));
        }
        return updateUser;
    }

    public User getUser(int userId) {
        if (!users.containsKey(userId)) {
            log.warn("user not found");
            throw new NotFoundException(String.format(
                    "User with id: %s not found", userId));
        }
        log.info("User found");
        return users.get(userId);
    }

    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }

    public void generateId() {
        id++;
    }
}
