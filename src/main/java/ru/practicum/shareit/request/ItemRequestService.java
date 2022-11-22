package ru.practicum.shareit.request;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ItemRequestService {
    private final ItemRequestRepository requestRepository;
    private final UserRepository userRepository;

    public ItemRequestService(ItemRequestRepository requestRepository, UserRepository userRepository) {
        this.requestRepository = requestRepository;
        this.userRepository = userRepository;
    }

    public ItemRequestDto create(ItemRequestDto requestDto, int userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format(
                        "User with id: %s not found", userId)));
        requestDto.setRequester(user);
        requestDto.setCreated(LocalDateTime.now());
            return ItemRequestMapper.toItemRequestDto(requestRepository.save(ItemRequestMapper.toItemRequest(requestDto)));
//        } catch (DataIntegrityViolationException e) {
//            log.error("Duplicated Email");
//            throw new ConflictException("Email can't duplicated");
//        }
    }

    public List<ItemRequestDto> getAll(int userId, int from, int size) {
        if (from<0||size<1){
            log.warn("Incorrect pagination parameters");
            throw new BadRequestException("Incorrect pagination parameters");
        }
        return requestRepository.findAll().stream()
                .map(ItemRequestMapper::toItemRequestDto)
                .collect(Collectors.toList());
    }


    public List<ItemRequestDto> getOwn(int userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format(
                        "User with id: %s not found", userId)));
        log.info("ItemRequest found");
        return requestRepository.getItemRequestByRequester_Id(userId).stream()
                .map(ItemRequestMapper::toItemRequestDto)
                .collect(Collectors.toList());
    }
}
