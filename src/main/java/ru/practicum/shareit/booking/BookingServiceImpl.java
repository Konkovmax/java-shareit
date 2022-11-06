package ru.practicum.shareit.booking;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingIncomeDto;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Slf4j
@Service
public class BookingServiceImpl implements BookingService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;

    public BookingServiceImpl(ItemRepository itemRepository, BookingRepository bookingRepository,
                              UserRepository userRepository) {
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
        this.bookingRepository = bookingRepository;

    }

    public BookingDto create(BookingIncomeDto bookingIncome, int userId) {
        throwIfNotValid(bookingIncome);
        int itemId = bookingIncome.getItemId();
        if (itemRepository.findById(itemId).isEmpty()) {
            log.warn("item not found");
            throw new NotFoundException(String.format(
                    "Item with id: %s not found", itemId));
        }
        if (!itemRepository.findById(itemId).get().getAvailable()) {
            log.warn("Item is not available");
            throw new BadRequestException(String.format(
                    "Item with id: %s is not available",
                    bookingIncome.getItemId()));
        }
        Booking booking = BookingMapper.IncomeToBooking(bookingIncome);
        booking.setBooker(userRepository.findById(userId).get());
        booking.setStatus(Status.WAITING);
        booking.setItem(itemRepository.findById(itemId).get());
        log.warn("Booking created");
        return BookingMapper.toBookingDto(bookingRepository.save(booking));
    }

    private void throwIfNotValid(BookingIncomeDto booking) throws BadRequestException {
        if (booking.getEnd().isBefore(LocalDateTime.now())) {
            log.error("Booking ending can't be in the past");
            throw new BadRequestException("Booking ending can't be in the past");
        }
        if (booking.getStart().isBefore(LocalDateTime.now())) {
            log.error("Booking start can't be in the past");
            throw new BadRequestException("Booking start can't be in the past");
        }
        if (booking.getStart().isAfter(booking.getEnd())) {
            log.error("Booking start can't be after end");
            throw new BadRequestException("Booking start can't be after end");
        }
    }

    public List<BookingDto> getAll(int userId, String stateIncome, boolean isOwner) {
        if (userRepository.findById(userId).isEmpty()) {
            log.warn("user not found");
            throw new NotFoundException(String.format(
                    "User with id: %s not found", userId));
        }
        State state;
        try {
        state = State.valueOf(stateIncome);
                    } catch (IllegalArgumentException e){
            throw new BadRequestException("Unknown state: UNSUPPORTED_STATUS");
                    //UnsupportedOperationException("Unknown state: UNSUPPORTED_STATUS");
        }
        Predicate<Booking> userType;
        if(isOwner) {
            userType = x -> x.getItem().getOwner().getId() == userId;
        }else{
            userType = x -> x.getBooker().getId() == userId;
        }
        if (state == State.FUTURE) {
            return bookingRepository.findAll().stream()
                    .filter(userType)
//                    .filter(x -> x.getBooker().getId() == userId)
                   // .filter(x -> x.getStatus() == Status.APPROVED)
                    .filter(x -> x.getStart().isAfter(LocalDateTime.now()))
                    .map(object -> BookingMapper.toBookingDto(object))
                    .sorted((x1, x2) -> x2.getStart().compareTo(x1.getStart()))
                    .collect(Collectors.toList());
        } else {
            return bookingRepository.findAll().stream()
                    .filter(userType)
                    .map(object -> BookingMapper.toBookingDto(object))
                    .sorted((x1, x2) -> x2.getStart().compareTo(x1.getStart()))
                    .collect(Collectors.toList());
        }
    }

    public BookingDto update(int bookingId, int userId, boolean approved) {
        if (bookingRepository.findById(bookingId).get().getItem().getOwner().getId() != userId
        ) {
            log.warn("user mismatched");
            throw new NotFoundException(String.format(
                    "User with id: %s does not own this item",
                    userId));
        }
        Booking updateBooking = bookingRepository.findById(bookingId).get();
        if(approved){
            updateBooking.setStatus(Status.APPROVED);
        }else {
            updateBooking.setStatus(Status.REJECTED);
        }
        log.info("Booking updated");
        return BookingMapper.toBookingDto(bookingRepository.save(updateBooking));
    }

    public BookingDto getBooking(int bookingId, int userId) {
        if (bookingRepository.findById(bookingId).isEmpty()) {
            log.warn("booking not found");
            throw new NotFoundException(String.format(
                    "Booking with id: %s not found", bookingId));
        }
        if ((bookingRepository.findById(bookingId).get().getBooker().getId() != userId)&&
                (bookingRepository.findById(bookingId).get().getItem().getOwner().getId()!= userId)
        ) {
            log.warn("user mismatched");
            throw new NotFoundException(String.format(
                    "User with id: %s does not booked this item",
                    userId));
        }

        return BookingMapper.toBookingDto(bookingRepository.findById(bookingId).get());
    }
}
