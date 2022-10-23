package ru.practicum.shareit.item;

import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemServiceImpl itemService;

    public ItemController(ItemServiceImpl itemService) {
        this.itemService = itemService;
    }

    @PostMapping
    public ItemDto create(@Valid @RequestBody Item item, @RequestHeader(value = "X-Sharer-User-Id",
            required = true) int userId) {
        return itemService.create(item, userId);
    }

    @GetMapping
    public List<ItemDto> getAll(@RequestHeader(value = "X-Sharer-User-Id", required = true) int userId) {
        return itemService.getAll(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> search(@RequestParam(value = "text", required = true) String query) {
        return itemService.search(query);
    }

    @GetMapping("/{id}")
    public ItemDto get(@PathVariable("id") Integer itemId) {
        return itemService.getItem(itemId);
    }

    @PatchMapping("/{id}")
    public ItemDto update(@PathVariable int id,
                          @RequestBody Item item,
                          @RequestHeader(value = "X-Sharer-User-Id", required = true) int userId) {
        return itemService.update(id, item, userId);
    }

    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable("id") Integer itemId) {
        itemService.delete(itemId);
    }
}
