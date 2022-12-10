package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingDateDto;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final ItemRequestRepository itemRequestRepository;

    public ItemServiceImpl(ItemRepository itemRepository, BookingRepository bookingRepository,
                           UserRepository userRepository, CommentRepository commentRepository,
                           ItemRequestRepository itemRequestRepository) {
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
        this.bookingRepository = bookingRepository;
        this.commentRepository = commentRepository;
        this.itemRequestRepository = itemRequestRepository;
    }

    public ItemDto create(ItemDto item, int userId) {
        User user = userRepository.findById(userId).orElseThrow();
        item.setOwner(user);
        ItemRequest request = null;
        if (item.getRequestId() != 0) {
            request = itemRequestRepository.findById(item.getRequestId())
                    .orElseThrow(() -> {
                        throw new NotFoundException(String.format(
                                "Request with id: %s not found", item.getRequestId()));
                    });
        }
        Item newItem = itemRepository.save(ItemMapper.toItem(item, request));
        return ItemMapper.toItemDto(newItem);
    }

    public CommentDto createComment(int itemId, CommentDto commentDto, int userId) {
        if (bookingRepository.getBookingsByItem_IdAndBooker_IdAndStatus_ApprovedIs(
                itemId, userId, LocalDateTime.now()).isEmpty()) {
            log.warn("user does not booked this item");
            throw new BadRequestException(String.format(
                    "User with id: %s does not booked this item",
                    userId));
        }
        Comment newComment = new Comment();
        newComment.setText(commentDto.getText());
        newComment.setItem(itemRepository.findById(itemId).orElseThrow());
        newComment.setAuthor(userRepository.findById(userId).orElseThrow());
        newComment.setCreated(LocalDateTime.now());
        return ItemMapper.toCommentDto(commentRepository.save(newComment));
    }

    public List<ItemDto> getAll(int userId, Pageable pageable) {
        Function<ItemDto, ItemDto> addBooking = i -> {
            List<BookingDateDto> bookings =
                    bookingRepository.getBookingsByItem_IdOrderByStart(i.getId()).stream()
                            .filter(x -> x.getItem().getOwner().getId() == userId)
                            .map(BookingMapper::toBookingDateDto)
                            .collect(Collectors.toList());
            if (!bookings.isEmpty()) {
                i.setLastBooking(bookings.get(0));
                i.setNextBooking(bookings.get(bookings.size() - 1));
            }
            return i;
        };
        return itemRepository.getItemByOwner_Id(userId, pageable).stream()
                .map(ItemMapper::toItemDto)
                .map(addBooking)
                .collect(Collectors.toList());
    }

    public List<ItemDto> search(String query, int from, int size) {
        if (query.isEmpty()) {
            return new ArrayList<>();
        } else {
            String adaptedQuery = query.toLowerCase();
            return itemRepository.search(adaptedQuery, PageRequest.of((size > from) ? 0 : from / size, size)).stream()
                    .map(ItemMapper::toItemDto)
                    .collect(Collectors.toList());
        }
    }

    public ItemDto update(int id, ItemDto item, int userId) {
        if (itemRepository.findById(id).orElseThrow().getOwner().getId() != userId) {
            throw new NotFoundException(String.format(
                    "User with id: %s does not own this item",
                    userId));
        }
        Item updateItem = itemRepository.findById(id).orElseThrow();
        if (item.getName() != null) {
            updateItem.setName(item.getName());
        }
        if (item.getDescription() != null) {
            updateItem.setDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            updateItem.setAvailable(item.getAvailable());
        }
        log.info("Item updated");

        return ItemMapper.toItemDto(itemRepository.save(updateItem));
    }

    public void delete(int itemId) {
        itemRepository.deleteById(itemId);
    }

    public ItemDto getItem(int itemId, int userId) {
        itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException(String.format(
                "Item with id: %s not found", itemId)));
        ItemDto itemWithBooking = ItemMapper.toItemDto(itemRepository.findById(itemId).orElseThrow());
        List<BookingDateDto> bookings =
                bookingRepository.getBookingsByItem_IdOrderByStart(itemId).stream()
                        .filter(x -> x.getItem().getOwner().getId() == userId)
                        .map(BookingMapper::toBookingDateDto)
                        .collect(Collectors.toList());
        if (!bookings.isEmpty()) {
            itemWithBooking.setLastBooking(bookings.get(0));
            itemWithBooking.setNextBooking(bookings.get(bookings.size() - 1));
        }
        itemWithBooking.setComments(commentRepository.getCommentsByItem_Id(itemId).stream()
                .map(ItemMapper::toCommentDto)
                .collect(Collectors.toList()));
        return itemWithBooking;
    }
}
