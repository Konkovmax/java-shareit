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

//    public List<BookingDto> getAll(int userId) {
//        return bookingRepository.findAll().stream()
//                .filter(x -> x.getOwner().getId() == userId)
//                .map(object -> BookingMapper.toBookingDto(object))
//                .collect(Collectors.toList());
//    }
//
//    public List<BookingDto> search(String query) {
//        if (query.isEmpty()) {
//            return new ArrayList<>();
//        } else {
//            String adaptedQuery = query.toLowerCase();
//            return bookingRepository.search(adaptedQuery).stream()
//                    .map(object -> BookingMapper.toBookingDto(object))
//                    .collect(Collectors.toList());
//        }
//    }
//
//    public BookingDto update(int id, BookingDto booking, int userId) {
////        if (!userStorage.users.containsKey(userId)) {
////            log.warn("user not found");
////            throw new NotFoundException(String.format(
////                    "User with id: %s not found",
////                    userId));
////        }
//        if (//bookingRepository.findById(id).isEmpty()
//        //        ||
//        bookingRepository.findById(id).get().getOwner().getId() != userId
//        ) {
//            log.warn("user mismatched");
//            throw new NotFoundException(String.format(
//                    "User with id: %s does not own this booking",
//                    userId));
//        }
////        if (!bookingRepository.bookings.containsKey(id)) {
////            log.warn("booking not found");
////            throw new NotFoundException(String.format(
////                    "Booking with id: %s not found",
////                    id));
////        }
//        Booking updateBooking = bookingRepository.findById(id).get();
//
//        if (booking.getName() != null) {
//            updateBooking.setName(booking.getName());
//        }
//        if (booking.getDescription() != null) {
//            updateBooking.setDescription(booking.getDescription());
//        }
//        if (booking.getAvailable() != null) {
//            updateBooking.setAvailable(booking.getAvailable());
//        }
//        log.info("Booking updated");
//
//        return BookingMapper.toBookingDto(bookingRepository.save(updateBooking));
//    }
//
//    public void delete(int bookingId) {
////        if (bookingRepository.bookings.containsKey(bookingId)) {
////            log.warn("booking not found");
////            throw new NotFoundException(String.format(
////                    "Booking with id: %s not found",
////                    bookingId));
////        }
//        bookingRepository.deleteById(bookingId);
//    }
//
//    public BookingDto getBooking(int bookingId) {
//        if (bookingRepository.findById(bookingId).isEmpty()) {
//            log.warn("booking not found");
//            throw new NotFoundException(String.format(
//                    "Booking with id: %s not found", bookingId));
//        }
//        return BookingMapper.toBookingDto(bookingRepository.findById(bookingId).get());
//    }
}
