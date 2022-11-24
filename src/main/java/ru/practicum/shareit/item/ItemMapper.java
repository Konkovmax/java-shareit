package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.ItemRequestRepository;

@Slf4j
public class ItemMapper {
    private static ItemRequestRepository itemRequestRepository;

    public ItemMapper(ItemRequestRepository itemRequestRepository) {
        this.itemRequestRepository = itemRequestRepository;
    }

    public static ItemDto toItemDto(Item item) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getOwner(),
                item.getRequest() != null ? item.getRequest().getId() : 0,
                null,
                null,
                null

        );
    }

    public static Item toItem(ItemDto item,ItemRequest request) {

        //ItemRequest request = null;

        return new Item(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getOwner(),
                item.getRequestId() != 0 ?
                request
//                        itemRequestRepository.findById(item.getRequestId())
//                        .orElseThrow(() -> {
//                            log.warn( "Request with id: %s not found", item.getRequestId());
//                            throw new NotFoundException(String.format(
//                                    "Request with id: %s not found", item.getRequestId()));
//                        })
                        : null
        );
    }

    public static CommentDto toCommentDto(Comment comment) {
        return new CommentDto(
                comment.getId(),
                comment.getText(),
                comment.getAuthor().getName(),
                comment.getCreated()
        );
    }
}
