package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.UserDto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class InMemoryItemStorage {
    public Map<Integer, ItemDto> items = new HashMap<>();
    private int id = 1;

    public ItemDto create(ItemDto item, int userId) {
        // throwIfNotValid(item);
        item.setId(id);
        generateId();
        items.put(item.getId(), item);
        return item;
    }

    public void delete(int itemId) {
        if (items.containsKey(itemId)) {
            items.remove(itemId);
            log.info("Item deleted");
        } else {
            log.warn("item not found");
            throw new NotFoundException(String.format(
                    "Item with id: %s not found",
                    itemId));
        }
    }

    public boolean throwIfNotValid(ItemDto newItem) throws BadRequestException {
//        for (ItemDto item: items.values()) {
//            if (item.getEmail().equals(newItem.getEmail())) {
//                log.error("Duplicated Email");
//                throw new BadRequestException("Email can't duplicated");
//            }
//        }
////        if (item.getLogin().isEmpty()) {
//            log.error("login can't be empty");
//            throw new BadRequestException("login can't be empty");
//        }
//        if (item.getBirthday().isAfter(LocalDate.now())) {
//            log.error("Birthday can't be in the Future");
//            throw new BadRequestException("Birthday can't be in the Future");
//        }
//        if (!item.getEmail().contains("@")) {
//            log.error("email should contain @");
//            throw new BadRequestException("email should contain @");
//        }
//        if (item.getLogin().contains(" ")) {
//            log.error("login can't contain spaces");
//            throw new BadRequestException("login can't contain spaces");
//        }
        return true;

    }
    public ItemDto update(int id, ItemDto item) {
        ItemDto updateItem = new ItemDto();
        if (items.containsKey(id)) {
            updateItem= items.get(id);
            updateItem.setId(id);
            if(item.getName()!=null){
                updateItem.setName(item.getName());
            }
            if(item.getDescription()!=null){
                updateItem.setDescription(item.getDescription());
            }
            if(item.getAvailable()!=null){
                updateItem.setAvailable(item.getAvailable());
            }
            items.put(id, updateItem);
            log.info("Item updated");
        } else {
//todo change to ljambda
//todo            explain empty service or move to service
            log.warn("item not found");
            throw new NotFoundException(String.format(
                    "Item with id: %s not found",
                    id));
        }
        return updateItem;
    }

    public ItemDto getItem(int itemId) {
        if (!items.containsKey(itemId)) {
            log.warn("item not found");
            throw new NotFoundException(String.format(
                    "Item with id: %s not found", itemId));
        }
        log.info("Item found");
        return items.get(itemId);
    }

    public List<ItemDto> getAll() {
        return new ArrayList<>(items.values());
    }

    public void generateId() {
        id++;
    }
}
