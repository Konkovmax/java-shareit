package ru.practicum.shareit.booking;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingIncomeDto;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
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
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException(String.format(
                        "Item with id: %s not found", itemId)));
        if (!item.getAvailable()) {
            log.warn("Item is not available");
            throw new BadRequestException(String.format(
                    "Item with id: %s is not available",
                    bookingIncome.getItemId()));
        }
        if (item.getOwner().getId() == userId) {
            log.warn("user mismatched");
            throw new NotFoundException(String.format(
                    "User with id: %s does already own this item",
                    userId));
        }
        Booking booking = BookingMapper.incomeToBooking(bookingIncome);
        booking.setBooker(userRepository.findById(userId).orElseThrow());
        booking.setStatus(Status.WAITING);
        booking.setItem(itemRepository.findById(itemId).orElseThrow());
        log.warn("Booking created");
        return BookingMapper.toBookingDto(bookingRepository.save(booking));
    }

    public List<BookingDto> getAllForUser(int userId, Pageable pageable, String stateIncome) {

        userRepository.findById(userId)
                .orElseThrow(() -> {
                    throw new NotFoundException(String.format(
                            "User with id: %s not found", userId));
                });
        return bookingRepository.getBookingByBooker_Id(userId,
                        pageable)
                .stream()
                .filter(bookingStatus(stateIncome))
                .map(BookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }

    public List<BookingDto> getAllForOwner(int userId, int from, int size, String stateIncome) {
        if (from < 0 || size < 1) {
            log.warn("Incorrect pagination parameters");
            throw new BadRequestException("Incorrect pagination parameters");
        }
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format(
                        "User with id: %s not found", userId)));
        return bookingRepository.getBookingByOwner_Id(userId,
                        PageRequest.of((size > from) ? 0 : from / size, size, Sort.by("start").descending())).stream()
                .filter(bookingStatus(stateIncome))
                .map(BookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }

    public BookingDto update(int bookingId, int userId, boolean approved) {
        if (bookingRepository.findById(bookingId).orElseThrow().getItem().getOwner().getId() != userId) {
            throw new NotFoundException(String.format(
                    "User with id: %s does not own this item",
                    userId));
        }
        if (bookingRepository.findById(bookingId).orElseThrow().getStatus() == Status.APPROVED) {
            throw new BadRequestException(String.format("Booking with id: %s already approved", bookingId));
        }
        Booking updateBooking = bookingRepository.findById(bookingId).orElseThrow();
        if (approved) {
            updateBooking.setStatus(Status.APPROVED);
        } else {
            updateBooking.setStatus(Status.REJECTED);
        }
        log.info("Booking updated");
        return BookingMapper.toBookingDto(bookingRepository.save(updateBooking));
    }

    public BookingDto getBooking(int bookingId, int userId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException(String.format(
                        "Booking with id: %s not found", bookingId)));
        if ((booking.getBooker().getId() != userId) &&
                (booking.getItem().getOwner().getId() != userId)
        ) {
            log.warn("user mismatched");
            throw new NotFoundException(String.format(
                    "User with id: %s does not booked this item",
                    userId));
        }
        return BookingMapper.toBookingDto(booking);
    }

    public void throwIfNotValid(BookingIncomeDto booking) throws BadRequestException {
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

    public Predicate<Booking> bookingStatus(String stateIncome) {
        State state;
        try {
            state = State.valueOf(stateIncome);
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Unknown state: UNSUPPORTED_STATUS");
        }
        Predicate<Booking> bookingStatus;
        switch (state) {
            case FUTURE: {
                bookingStatus = x -> x.getStart().isAfter(LocalDateTime.now());
                break;
            }
            case CURRENT: {
                bookingStatus = x -> x.getStart().isBefore(LocalDateTime.now())
                        && x.getEnd().isAfter(LocalDateTime.now());
                break;
            }
            case PAST: {
                bookingStatus = x -> x.getEnd().isBefore(LocalDateTime.now())
                        && (x.getStatus() == Status.APPROVED);
                break;
            }
            case WAITING: {
                bookingStatus = x -> x.getStatus() == Status.WAITING;
                break;
            }
            case REJECTED: {
                bookingStatus = x -> x.getStatus() == Status.REJECTED;
                break;
            }
            default: {
                bookingStatus = x -> true;
                break;
            }
        }
        return bookingStatus;
    }
}
