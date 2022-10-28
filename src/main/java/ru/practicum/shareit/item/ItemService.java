package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {

    ItemDto create(ItemDto item, int userId);

    List<ItemDto> getAll(int userId);

    List<ItemDto> search(String query);

    ItemDto update(int id, ItemDto item, int userId);

    void delete(int itemId);

    ItemDto getItem(int itemId);

}
