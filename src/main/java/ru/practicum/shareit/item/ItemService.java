package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.InMemoryUserStorage;
import ru.practicum.shareit.user.UserMapper;

import java.util.List;
@Slf4j
@Service
public class ItemService {
    private final InMemoryItemStorage itemStorage;
  private final InMemoryUserStorage userStorage;

    public ItemService(InMemoryItemStorage itemStorage, InMemoryUserStorage userStorage) {
        this.itemStorage = itemStorage;
        this.userStorage = userStorage;
    }

    public ItemDto create(ItemDto item, int userId){
        if (userStorage.users.containsKey(userId)) {
            item.setOwner(UserMapper.toUserDto(userStorage.getUser(userId)));
        return itemStorage.create(item, userId);
                   } else {
            log.warn("user not found");
            throw new NotFoundException(String.format(
                    "User with id: %s not found",
                    userId));
        }
    }

    public List<ItemDto> getAll(){
        return itemStorage.getAll();
    }

    public ItemDto update(int id,ItemDto item, int userId){
        if (userStorage.users.containsKey(userId)) {
            if (itemStorage.items.get(id).getOwner().)
            return itemStorage.update(id, item);
        } else {
            log.warn("user not found");
            throw new NotFoundException(String.format(
                    "User with id: %s not found",
                    userId));
        }
    }
    public void delete(int itemId){
        itemStorage.delete(itemId);
    }

    public ItemDto getItem(int itemId){
        return itemStorage.getItem(itemId);
    }
}
