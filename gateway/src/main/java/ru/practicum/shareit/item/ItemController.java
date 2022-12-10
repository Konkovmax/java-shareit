package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemClient itemService;

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody @Valid ItemDto item,
                                         @RequestHeader(value = "X-Sharer-User-Id") int userId) {
        return itemService.create(item, userId);
    }

    @PostMapping("{itemId}/comment")
    public ResponseEntity<Object> createComment(@PathVariable("itemId") Integer itemId,
                                                @RequestBody @Valid CommentDto commentDto,
                                                @RequestHeader(value = "X-Sharer-User-Id") int userId) {
        return itemService.createComment(itemId, commentDto, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getAll(@RequestHeader(value = "X-Sharer-User-Id") int userId,
                                         @PositiveOrZero @RequestParam(value = "from", defaultValue = "0", required = false) int from,
                                         @Positive @RequestParam(value = "size", defaultValue = "10", required = false) int size) {
        return itemService.getAll(userId, from, size);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> search(@RequestParam(value = "text") String query,
                                         @PositiveOrZero @RequestParam(value = "from", defaultValue = "0", required = false) int from,
                                         @Positive @RequestParam(value = "size", defaultValue = "10", required = false) int size) {
        return itemService.search(query, from, size);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> get(@PathVariable("id") Integer itemId,
                                      @RequestHeader(value = "X-Sharer-User-Id") int userId) {
        return itemService.getItem(itemId, userId);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable int id,
                                         @RequestBody ItemDto item,
                                         @RequestHeader(value = "X-Sharer-User-Id") int userId) {
        return itemService.update(id, item, userId);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Object> delete(@PathVariable("id") Integer itemId) {
        return itemService.delete(itemId);
    }
}
