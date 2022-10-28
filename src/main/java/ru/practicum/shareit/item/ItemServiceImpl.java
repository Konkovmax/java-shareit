package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.InMemoryUserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ItemServiceImpl implements ItemService {
    private final InMemoryItemStorage itemStorage;
    private final InMemoryUserStorage userStorage;

    public ItemServiceImpl(InMemoryItemStorage itemStorage, InMemoryUserStorage userStorage) {
        this.itemStorage = itemStorage;
        this.userStorage = userStorage;
    }

    public ItemDto create(ItemDto item, int userId) {
        if (!userStorage.users.containsKey(userId)) {
            log.warn("user not found");
            throw new NotFoundException(String.format(
                    "User with id: %s not found",
                    userId));
        }
        item.setOwner(userStorage.getUser(userId));
        return itemStorage.create(ItemMapper.toItem(item));
    }

    public List<ItemDto> getAll(int userId) {
        return itemStorage.getAll(userId).stream()
                .map(object -> ItemMapper.toItemDto(object))
                .collect(Collectors.toList());
    }

    public List<ItemDto> search(String query) {
        if (query.isEmpty()) {
            return new ArrayList<>();
        } else {
            String adaptedQuery = query.toLowerCase();
            return itemStorage.search(adaptedQuery).stream()
                    .map(object -> ItemMapper.toItemDto(object))
                    .collect(Collectors.toList());
        }
    }

    public ItemDto update(int id, ItemDto item, int userId) {
        if (!userStorage.users.containsKey(userId)) {
            log.warn("user not found");
            throw new NotFoundException(String.format(
                    "User with id: %s not found",
                    userId));
        }
        if (itemStorage.items.get(id).getOwner().getId() != userId) {
            log.warn("user mismatched");
            throw new NotFoundException(String.format(
                    "User with id: %s does not own this item",
                    userId));
        }
        if (!itemStorage.items.containsKey(id)) {
            log.warn("item not found");
            throw new NotFoundException(String.format(
                    "Item with id: %s not found",
                    id));
        }
        return ItemMapper.toItemDto(itemStorage.update(id, ItemMapper.toItem(item)));
    }

    public void delete(int itemId) {
        if (itemStorage.items.containsKey(itemId)) {
            log.warn("item not found");
            throw new NotFoundException(String.format(
                    "Item with id: %s not found",
                    itemId));
        }
        itemStorage.delete(itemId);
    }

    public ItemDto getItem(int itemId) {
        return ItemMapper.toItemDto(itemStorage.getItem(itemId));
    }
}
