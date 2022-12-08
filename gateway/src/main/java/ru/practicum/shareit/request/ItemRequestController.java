package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import javax.validation.Valid;
import java.util.List;


@Controller
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemRequestController {
    private final RequestClient requestService;

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody @Valid ItemRequestDto requestDto,
                                         @RequestHeader(value = "X-Sharer-User-Id") int userId) {
        return requestService.create(requestDto, userId);
    }

    @GetMapping
    public ResponseEntity<Object>  getOwn(@RequestHeader(value = "X-Sharer-User-Id") int userId) {
        return requestService.getOwn(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object>  getAll(@RequestHeader(value = "X-Sharer-User-Id") int userId,
                                       @RequestParam(value = "from", defaultValue = "0", required = false) int from,
                                       @RequestParam(value = "size", defaultValue = "10", required = false) int size) {
        return requestService.getAll(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object>  get(@PathVariable("requestId") Integer requestId,
                              @RequestHeader(value = "X-Sharer-User-Id") int userId) {
        return requestService.get(requestId, userId);
    }

}
