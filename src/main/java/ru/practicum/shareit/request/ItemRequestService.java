package ru.practicum.shareit.request;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
//наверное лучше тут сразу пометить, что по срокам ужас как всё горело, хотя это и так будет очевидно
@Service
@Slf4j
public class ItemRequestService {
    private final ItemRequestRepository requestRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    public ItemRequestService(ItemRequestRepository requestRepository, UserRepository userRepository,
                              ItemRepository itemRepository) {
        this.requestRepository = requestRepository;
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
    }

    public ItemRequestDto create(ItemRequestDto requestDto, int userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format(
                        "User with id: %s not found", userId)));
        requestDto.setRequester(user);
        requestDto.setCreated(LocalDateTime.now());
        return ItemRequestMapper.toItemRequestDto(requestRepository.save(ItemRequestMapper.toItemRequest(requestDto)));
    }

    public List<ItemRequestDto> getAll(int userId, int from, int size) {
        if (from < 0 || size < 1) {
            log.warn("Incorrect pagination parameters");
            throw new BadRequestException("Incorrect pagination parameters");
        }
        return requestRepository.findItemRequestByRequester_IdNot(userId,
                        PageRequest.of((size > from) ? 0 : from / size, size, Sort.by("created").descending())).stream()
                .map(ItemRequestMapper::toItemRequestDto)
                .peek(x -> x.setItems(itemRepository.getItemByRequest_Id(x.getId()).stream()
                        .map(ItemMapper::toItemDto)
                        .collect(Collectors.toList())))
                .collect(Collectors.toList());
    }


    public List<ItemRequestDto> getOwn(int userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format(
                        "User with id: %s not found", userId)));
        log.info("ItemRequest found");
        return requestRepository.getItemRequestByRequester_Id(userId).stream()
                .map(ItemRequestMapper::toItemRequestDto)
                .peek(x -> x.setItems(itemRepository.getItemByRequest_Id(x.getId()).stream()
                        .map(ItemMapper::toItemDto)
                        .collect(Collectors.toList())))
                .collect(Collectors.toList());
    }

    public ItemRequestDto get(int requestId, int userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format(
                        "User with id: %s not found", userId)));
        log.info("ItemRequest found");
        ItemRequestDto request = ItemRequestMapper.toItemRequestDto(requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException(String.format(
                        "Request with id: %s not found", requestId))));
        request.setItems(itemRepository.getItemByRequest_Id(request.getId()).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList()));
        return request;
    }
}
