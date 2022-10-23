package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
public class InMemoryItemStorage {
    public Map<Integer, Item> items = new HashMap<>();
    private int id = 1;

    public ItemDto create(Item item) {
        item.setId(id);
        generateId();
        items.put(item.getId(), item);
        return ItemMapper.toItemDto(item);
    }

    public void delete(int itemId) {
        if (items.containsKey(itemId)) {
            log.warn("item not found");
            throw new NotFoundException(String.format(
                    "Item with id: %s not found",
                    itemId));
        }
        items.remove(itemId);
        log.info("Item deleted");
    }

    public Item update(int id, Item item) {
        Item updateItem;
        if (items.containsKey(id)) {
            updateItem = items.get(id);
            updateItem.setId(id);
            if (item.getName() != null) {
                updateItem.setName(item.getName());
            }
            if (item.getDescription() != null) {
                updateItem.setDescription(item.getDescription());
            }
            if (item.getAvailable() != null) {
                updateItem.setAvailable(item.getAvailable());
            }
            items.put(id, updateItem);
            log.info("Item updated");
        } else {
            log.warn("item not found");
            throw new NotFoundException(String.format(
                    "Item with id: %s not found",
                    id));
        }
        return updateItem;
    }

    public Item getItem(int itemId) {
        if (!items.containsKey(itemId)) {
            log.warn("item not found");
            throw new NotFoundException(String.format(
                    "Item with id: %s not found", itemId));
        }
        log.info("Item found");
        return items.get(itemId);
    }

    public List<Item> getAll(int userId) {
        return items.values().stream().filter(x -> x.getOwner().getId() == userId)
                .collect(Collectors.toList());
    }

    public List<Item> search(String query) {
        return items.values().stream()
                .filter(x -> (((x.getName().toLowerCase().contains(query))
                        || x.getDescription().toLowerCase().contains(query))
                        && (x.getAvailable())))
                .collect(Collectors.toList());
    }

    public void generateId() {
        id++;
    }
}
