package ru.practicum.shareit.request;

import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import javax.validation.Valid;


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

//    @GetMapping
//    public List<ItemRequestDto> getAll(@RequestHeader(value = "X-Sharer-User-Id") int userId,
//                                   @RequestParam(value = "state", defaultValue = "ALL", required = false) String state) {
//        return requestService.getAllForUser(userId, state);
//    }
//
//    @GetMapping("/owner")
//    public List<ItemRequestDto> getAllOwners(@RequestHeader(value = "X-Sharer-User-Id") int userId,
//                                         @RequestParam(value = "state", defaultValue = "ALL", required = false) String state) {
//        return requestService.getAllForOwner(userId, state);
//    }
//
//    @GetMapping("/{bookingId}")
//    public ItemRequestDto get(@PathVariable("bookingId") Integer bookingId,
//                          @RequestHeader(value = "X-Sharer-User-Id") int userId) {
//        return requestService.getItemRequest(bookingId, userId);
//    }

}
