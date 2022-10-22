package ru.practicum.shareit.item;

import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.UserDto;
import ru.practicum.shareit.user.UserService;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping
    public ItemDto create(@Valid @RequestBody ItemDto item, @RequestHeader(value = "X-Sharer-User-Id",
            required = true) int userId) {
        return itemService.create(item, userId);
    }

    @GetMapping
    public List<ItemDto> getAll() {
        return itemService.getAll();
    }

    @GetMapping("/{id}")
    public ItemDto get(@PathVariable("id") Integer itemId) {
        return itemService.getItem(itemId);
    }

    @PatchMapping("/{id}")
    public ItemDto update(@PathVariable int id,
                          @RequestBody ItemDto item,
                          @RequestHeader(value = "X-Sharer-User-Id", required = true) int userId) {
        return itemService.update(id,item, userId);
    }

    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable("id") Integer itemId) {
        itemService.delete(itemId);
    }
}
