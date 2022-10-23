package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {

    ItemDto create(Item item, int userId);

    List<ItemDto> getAll(int userId);

    List<ItemDto> search(String query);

    ItemDto update(int id,Item item, int userId);

    void delete(int itemId);

    ItemDto getItem(int itemId);

}
