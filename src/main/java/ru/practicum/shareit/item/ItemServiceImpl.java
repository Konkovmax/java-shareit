package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.InMemoryUserStorage;
import ru.practicum.shareit.user.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    public ItemServiceImpl(ItemRepository itemRepository, UserRepository userRepository) {
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;

    }

    public ItemDto create(ItemDto item, int userId) {
//        if (!userStorage.users.containsKey(userId)) {
//            log.warn("user not found");
//            throw new NotFoundException(String.format(
//                    "User with id: %s not found",
//                    userId));
//        }
        item.setOwner(userRepository.findById(userId).get());
        return ItemMapper.toItemDto(itemRepository.save(ItemMapper.toItem(item)));
    }

    public List<ItemDto> getAll(int userId) {
        return itemRepository.findAll().stream()
                .filter(x -> x.getOwner().getId() == userId)
                .map(object -> ItemMapper.toItemDto(object))
                .collect(Collectors.toList());
    }

    public List<ItemDto> search(String query) {
        if (query.isEmpty()) {
            return new ArrayList<>();
        } else {
            String adaptedQuery = query.toLowerCase();
            return itemRepository.search(adaptedQuery).stream()
                    .map(object -> ItemMapper.toItemDto(object))
                    .collect(Collectors.toList());
        }
    }

    public ItemDto update(int id, ItemDto item, int userId) {
//        if (!userStorage.users.containsKey(userId)) {
//            log.warn("user not found");
//            throw new NotFoundException(String.format(
//                    "User with id: %s not found",
//                    userId));
//        }
        if (//itemRepository.findById(id).isEmpty()
        //        ||
        itemRepository.findById(id).get().getOwner().getId() != userId
        ) {
            log.warn("user mismatched");
            throw new NotFoundException(String.format(
                    "User with id: %s does not own this item",
                    userId));
        }
        Item updateItem = itemRepository.findById(id).get();

        if (item.getName() != null) {
            updateItem.setName(item.getName());
        }
        if (item.getDescription() != null) {
            updateItem.setDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            updateItem.setAvailable(item.getAvailable());
        }
        log.info("Item updated");

        return ItemMapper.toItemDto(itemRepository.save(updateItem));
    }

    public void delete(int itemId) {
       itemRepository.deleteById(itemId);
    }

    public ItemDto getItem(int itemId) {
        if (itemRepository.findById(itemId).isEmpty()) {
            log.warn("item not found");
            throw new NotFoundException(String.format(
                    "Item with id: %s not found", itemId));
        }
        return ItemMapper.toItemDto(itemRepository.findById(itemId).get());
    }
}
