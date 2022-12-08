package ru.practicum.shareit.item;

import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

@RestController
@RequestMapping(path = "/items")
public class ItemController {
    private final ItemServiceImpl itemService;

    public ItemController(ItemServiceImpl itemService) {
        this.itemService = itemService;
    }

    @PostMapping
    public ItemDto create(@RequestBody ItemDto item, @RequestHeader(value = "X-Sharer-User-Id") int userId) {
        return itemService.create(item, userId);
    }

    @PostMapping("{itemId}/comment")
    public CommentDto createComment(@PathVariable("itemId") Integer itemId,
                                    @RequestBody CommentDto commentDto,
                                    @RequestHeader(value = "X-Sharer-User-Id") int userId) {
        return itemService.createComment(itemId, commentDto, userId);
    }

    @GetMapping
    public List<ItemDto> getAll(@RequestHeader(value = "X-Sharer-User-Id") int userId,
                                @RequestParam(value = "from", defaultValue = "0", required = false) int from,
                                @RequestParam(value = "size", defaultValue = "10", required = false) int size) {
        return itemService.getAll(userId, from, size);
    }

    @GetMapping("/search")
    public List<ItemDto> search(@RequestParam(value = "text") String query,
                                @RequestParam(value = "from", defaultValue = "0", required = false) int from,
                                @RequestParam(value = "size", defaultValue = "10", required = false) int size) {
        return itemService.search(query, from, size);
    }

    @GetMapping("/{id}")
    public ItemDto get(@PathVariable("id") Integer itemId,
                       @RequestHeader(value = "X-Sharer-User-Id") int userId) {
        return itemService.getItem(itemId, userId);
    }

    @PatchMapping("/{id}")
    public ItemDto update(@PathVariable int id,
                          @RequestBody ItemDto item,
                          @RequestHeader(value = "X-Sharer-User-Id") int userId) {
        return itemService.update(id, item, userId);
    }

    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable("id") Integer itemId) {
        itemService.delete(itemId);
    }
}
