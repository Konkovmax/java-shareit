package ru.practicum.shareit.item;

import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

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
    public ItemDto create(@Valid @RequestBody ItemDto item, @RequestHeader(value = "X-Sharer-User-Id",
            required = true) int userId) {
        return itemService.create(item, userId);
    }

    @PostMapping("{itemId}/comment")
    public CommentDto createComment(@PathVariable("itemId") Integer itemId,
                                    @Valid @RequestBody CommentDto commentDto,
                                    @RequestHeader(value = "X-Sharer-User-Id", required = true) int userId) {
        return itemService.createComment(itemId, commentDto, userId);
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
    public ItemDto get(@PathVariable("id") Integer itemId,
                       @RequestHeader(value = "X-Sharer-User-Id", required = true) int userId) {
        return itemService.getItem(itemId, userId);
    }

    @PatchMapping("/{id}")
    public ItemDto update(@PathVariable int id,
                          @RequestBody ItemDto item,
                          @RequestHeader(value = "X-Sharer-User-Id", required = true) int userId) {
        return itemService.update(id, item, userId);
    }

    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable("id") Integer itemId) {
        itemService.delete(itemId);
    }
}
