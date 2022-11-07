package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.dto.BookingDateDto;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
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

    public ItemServiceImpl(ItemRepository itemRepository, BookingRepository bookingRepository,
                           UserRepository userRepository, CommentRepository commentRepository) {
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
        this.bookingRepository = bookingRepository;
        this.commentRepository = commentRepository;
    }

    public ItemDto create(ItemDto item, int userId) {
        item.setOwner(userRepository.findById(userId).get());
        return ItemMapper.toItemDto(itemRepository.save(ItemMapper.toItem(item)));
    }

    public CommentDto createComment(int itemId, CommentDto commentDto, int userId) {

        List<Booking> test = bookingRepository.getBookingsByItem_IdOrderByStart(itemId);
        if (bookingRepository.getBookingsByItem_IdOrderByStart(itemId).stream()
                .filter(x -> x.getBooker().getId() == userId)
                .filter(x -> x.getStatus() == Status.APPROVED)
                .filter(x -> x.getEnd().isBefore(LocalDateTime.now()))
                .collect(Collectors.toList()).isEmpty()) {
            log.warn("user does not booked this item");
            throw new BadRequestException(String.format(
                    "User with id: %s does not booked this item",
                    userId));
        }
        Comment newComment = new Comment();
                newComment.setText(commentDto.getText());
                newComment.setItem(itemRepository.findById(itemId).get());
                newComment.setAuthor(userRepository.findById(userId).get());
                newComment.setCreated(LocalDateTime.now());
        return ItemMapper.toCommentDto(commentRepository.save(newComment));
    }

    public List<ItemDto> getAll(int userId) {
        Function<ItemDto, ItemDto> addBooking = i -> {
            List<BookingDateDto> bookings =
                    bookingRepository.getBookingsByItem_IdOrderByStart(i.getId()).stream()
                            .filter(x -> x.getItem().getOwner().getId() == userId)
                            .map(object -> BookingMapper.toBookingDateDto(object))
                            .collect(Collectors.toList());
            if (!bookings.isEmpty()) {
                i.setLastBooking(bookings.get(0));
                i.setNextBooking(bookings.get(bookings.size() - 1));
            }
            return i;
        };
        return itemRepository.findAll().stream()
                .filter(x -> x.getOwner().getId() == userId)
                .map(object -> ItemMapper.toItemDto(object))
                .map(addBooking)
                .collect(Collectors.toList());
    }

    public List<ItemDto> search(String query) {
        if (query.isEmpty()) {
            return new ArrayList<>();
        } else {
            String adaptedQuery = query.toLowerCase();
            return itemRepository.search(adaptedQuery).stream()
                    .map(object -> ItemMapper.toItemDto(object))
                    .collect(Collectors.toList());
        }
    }

    public ItemDto update(int id, ItemDto item, int userId) {
        if (itemRepository.findById(id).get().getOwner().getId() != userId) {
            log.warn("user mismatched");
            throw new NotFoundException(String.format(
                    "User with id: %s does not own this item",
                    userId));
        }
        Item updateItem = itemRepository.findById(id).get();
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
        if (itemRepository.findById(itemId).isEmpty()) {
            log.warn("item not found");
            throw new NotFoundException(String.format(
                    "Item with id: %s not found", itemId));
        }
        ItemDto itemWithBooking = ItemMapper.toItemDto(itemRepository.findById(itemId).get());
        List<BookingDateDto> bookings =
                bookingRepository.getBookingsByItem_IdOrderByStart(itemId).stream()
                        .filter(x -> x.getItem().getOwner().getId() == userId)
                        .map(object -> BookingMapper.toBookingDateDto(object))
                        .collect(Collectors.toList());
        if (!bookings.isEmpty()) {
            itemWithBooking.setLastBooking(bookings.get(0));
            itemWithBooking.setNextBooking(bookings.get(bookings.size() - 1));
        }
        itemWithBooking.setComments(commentRepository.getCommentsByItem_Id(itemId).stream()
                .map(object -> ItemMapper.toCommentDto(object))
                .collect(Collectors.toList()));
        return itemWithBooking;
    }
}
