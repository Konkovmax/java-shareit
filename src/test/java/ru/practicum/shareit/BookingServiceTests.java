package ru.practicum.shareit;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingServiceImpl;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.dto.BookingDateDto;
import ru.practicum.shareit.booking.dto.BookingIncomeDto;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
//@Sql("/testschema.sql")
//@Sql("/testdata.sql")
class BookingServiceTests {
    private final BookingServiceImpl bookingService;

    @Mock
    private Booking mockBooking;

    @Test
    public void testBookingValidation() {
        BadRequestException ex = assertThrows(
                BadRequestException.class,
                () -> {
                    BookingIncomeDto booking = new BookingIncomeDto(LocalDateTime.now().minusDays(3),
                            LocalDateTime.now().plusDays(10), 1);
                    bookingService.throwIfNotValid(booking);
                });
        Assertions.assertEquals("Booking start can't be in the past", ex.getMessage());
    }

    @Test
    public void bookingStatusTest() {
        List<Booking> newBooking = Arrays.asList(
                new Booking(1, LocalDateTime.now(), LocalDateTime.now().minusDays(2),
                        null, null, Status.WAITING),
                new Booking(2, LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(3),
                        null, null, Status.WAITING));
        Predicate<Booking> status = bookingService.bookingStatus("FUTURE");
//            when(mockBooking.getStart().isAfter(LocalDateTime.now())).thenReturn(x -> x.getStart().isAfter(LocalDateTime.now()));
//            when(mockDTO.isCompleted()).thenReturn(false);
        List<Booking> filterBooking = newBooking.stream()
                .filter(status)
                .collect(Collectors.toList());
//            Predicate<Booking> expected = x -> x.getStart().isAfter(LocalDateTime.now());
//            boolean actual = isDone.test(mockDTO);

        Assertions.assertEquals(filterBooking.get(0).getId(), 2);
    }

    @Test
    public void bookingMapperTest() {
        User newUser = new User(1, "Name", "email@email.com");
                Booking booking = new Booking(1, null, null,
                null, newUser, Status.WAITING);
        BookingDateDto expectedBooking = new BookingDateDto(1, null, null,
                null, 1, Status.WAITING);
        Assertions.assertEquals(expectedBooking,BookingMapper.toBookingDateDto(booking));
    }
//    BadRequestException ex1 = assertThrows(
//                BadRequestException.class,
//                () -> {
//        BookingIncomeDto booking = new BookingIncomeDto(LocalDateTime.now(),LocalDateTime.now().minusDays(1),1);
//        bookingService.throwIfNotValid(booking);
//                });
//        Assertions.assertEquals("Booking ending can't be in the past", ex1.getMessage());
//    }

//    @Test
//    public void testFindAllUsers() {
//        List<User> allUsers = userStorage.findAll();
//        assertEquals(3, allUsers.size());
//    }
//
//    @Test
//    public void testCreateUser() {
//        int userId = 4;
//
//        User user = new User(userId, "Name", "login", "1989-02-01", "email@email.ru");
//        userStorage.create(user);
//        User savedUser = userStorage.getUser(userId).get();
//        savedUser.setId(userId);
//        assertEquals(user, savedUser, "Users not equal");
//    }
//
//    @Test
//    public void testUpdateUser() {
//        int userId = 3;
//
//        User user = new User(userId, "Name", "login", "1989-02-01", "email@email.ru");
//        userStorage.update(user);
//        User savedUser = userStorage.getUser(userId).get();
//        savedUser.setId(userId);
//        assertEquals(user, savedUser, "Users not equal");
//    }
//
//    @Test
//    public void testUserExistCheck() {
//        assertEquals(0, userStorage.userExistCheck(5));
//    }
//
//    @Test
//    public void testCommonFriends() {
//        List<User> commonFriends = userStorage.getCommonFriends(1, 2);
//        assertEquals("Ivan", commonFriends.get(0).getName());
//    }
//
//    @Test
//    public void testGetFriends() {
//        List<User> friends = userStorage.getFriends(1);
//        assertEquals(2, friends.size());
//    }
//
//    @Test
//    public void testAddFriend() {
//        userStorage.addFriend(3, 1);
//        assertEquals("Mario", userStorage.getFriends(3).get(0).getName());
//    }
//
//    @Test
//    public void testRemoveFriends() {
//        userStorage.removeFriend(1, 2);
//        assertEquals(1, userStorage.getFriends(1).size());
//    }
}
