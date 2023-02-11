package ru.practicum.shareit.item;

import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {

    ItemDto create(ItemDto item, int userId);

    List<ItemDto> getAll(int userId, Pageable pageable);

    List<ItemDto> search(String query, int from, int size);

    ItemDto update(int id, ItemDto item, int userId);

    void delete(int itemId);

    ItemDto getItem(int itemId, int userId);

}
