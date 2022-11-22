package ru.practicum.shareit.request;

import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final ItemRequestService requestService;

    public ItemRequestController(ItemRequestService requestService) {
        this.requestService = requestService;
    }

    @PostMapping
    public ItemRequestDto create(@Valid @RequestBody ItemRequestDto requestDto,
                                 @RequestHeader(value = "X-Sharer-User-Id") int userId) {
        return requestService.create(requestDto, userId);
    }

    @GetMapping
    public List<ItemRequestDto> getOwn(@RequestHeader(value = "X-Sharer-User-Id") int userId){
        return requestService.getOwn(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getAll(@RequestHeader(value = "X-Sharer-User-Id") int userId,
                                         @RequestParam(value = "from", defaultValue = "0", required = false) int from,
                                       @RequestParam(value = "size", defaultValue = "10", required = false) int size) {
        return requestService.getAll(userId, from, size);
    }

//    @GetMapping("/{bookingId}")
//    public ItemRequestDto get(@PathVariable("bookingId") Integer bookingId,
//                          @RequestHeader(value = "X-Sharer-User-Id") int userId) {
//        return requestService.getItemRequest(bookingId, userId);
//    }

}
