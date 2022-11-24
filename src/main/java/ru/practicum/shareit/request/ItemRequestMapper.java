package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.ItemRequestDto;

public class ItemRequestMapper {
    public static ItemRequestDto toItemRequestDto(ItemRequest request) {
        return new ItemRequestDto(
                request.getId(),
                request.getDescription(),
                request.getRequester(),
                request.getCreated(),
                null
//                request.getItem()
        );
    }

    public static ItemRequest toItemRequest(ItemRequestDto request) {
        return new ItemRequest(
                request.getId(),
                request.getDescription(),
                request.getRequester(),
                request.getCreated()
                // request.getItem()
        );
    }
}
