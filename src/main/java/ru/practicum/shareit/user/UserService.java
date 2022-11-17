package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDto create(UserDto user) {
        try {
            return UserMapper.toUserDto(userRepository.save(UserMapper.toUser(user)));
        } catch (DataIntegrityViolationException e) {
            log.error("Duplicated Email");
            throw new ConflictException("Email can't duplicated");
        }
    }

    public List<UserDto> getAll() {
        return userRepository.findAll().stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    public UserDto update(int id, UserDto user) {
        user.setId(id);
        User updateUser = userRepository.findById(id).orElseThrow();
        if (user.getName() != null) {
            updateUser.setName(user.getName());
        }
        if (user.getEmail() != null) {
            updateUser.setEmail(user.getEmail());
        }
        try {
            return UserMapper.toUserDto(userRepository.save(updateUser));
        } catch (DataIntegrityViolationException e) {
            log.error("Duplicated Email");
            throw new ConflictException("Email can't duplicated");
        }
    }

    public void delete(int userId) {
        userRepository.deleteById(userId);
    }

    public UserDto getUser(int userId) {
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException(String.format(
                "User with id: %s not found", userId)));
        log.info("User found");
        return UserMapper.toUserDto(userRepository.findById(userId).orElseThrow());
    }
}
