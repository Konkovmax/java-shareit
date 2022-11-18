package ru.practicum.shareit.request;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

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
            return ItemRequestMapper.toItemRequestDto(requestRepository.save(ItemRequestMapper.toItemRequest(requestDto)));
//        } catch (DataIntegrityViolationException e) {
//            log.error("Duplicated Email");
//            throw new ConflictException("Email can't duplicated");
//        }
    }

//    public List<ItemRequestDto> getAll() {
//        return requestRepository.findAll().stream()
//                .map(ItemRequestMapper::toItemRequestDto)
//                .collect(Collectors.toList());
//    }
//
//
//    public ItemRequestDto getItemRequest(int userId) {
//        requestRepository.findById(userId).orElseThrow(() -> new NotFoundException(String.format(
//                "ItemRequest with id: %s not found", userId)));
//        log.info("ItemRequest found");
//        return ItemRequestMapper.toItemRequestDto(requestRepository.findById(userId).orElseThrow());
//    }
}
