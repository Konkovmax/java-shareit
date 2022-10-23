package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
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
        // throwIfNotValid(item);
        item.setId(id);
        generateId();
        items.put(item.getId(), item);
        return ItemMapper.toItemDto(item);
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

    public boolean throwIfNotValid(Item newItem) throws BadRequestException {
//        for (Item item: items.values()) {
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
    public Item update(int id, Item item) {
        Item updateItem = new Item();
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
        return new ArrayList<>(items.values().stream().filter(x->x.getOwner().getId()==userId)
                .collect(Collectors.toList()));
    }

    public List<Item> search(String query) {

//      //  for (Item x:items.values()){
//String s1 = " дрель  аккумуляторная";
//String s2 = "Аккумуляторная";
//           if ((x.getName().toLowerCase().contains(query))
//                    |x.getDescription().toLowerCase().contains(query))
//            Boolean b = s1.matches("(?i).*" + s2 + ".*");
//            if (s1.matches("(?i).*" + s2 + ".*"))
//
//            {
//
//            log.warn("ok");
//            }
       //
        return new ArrayList<>(items.values().stream()
                .filter(x->(((x.getName().toLowerCase().contains(query))
                    |x.getDescription().toLowerCase().contains(query))
                &(x.getAvailable()==true)))
                .collect(Collectors.toList()));
    }

    public void generateId() {
        id++;
    }
}
