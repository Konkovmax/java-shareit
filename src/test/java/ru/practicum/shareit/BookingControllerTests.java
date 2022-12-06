package ru.practicum.shareit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class BookingControllerTests {

    private static final ObjectMapper om = JsonMapper.builder()
            .addModule(new JavaTimeModule())
            .build();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookingRepository mockRepository;

    @MockBean
    private UserRepository mockUserRepository;

    @MockBean
    private ItemRepository mockItemRepository;

    @Test
    public void getBookingTestOK() throws Exception {
        User newUser = new User(1, "Name", "email@email.com");
        Item newItem = new Item(1, "Name", "Description", true, newUser, null);

        BookingDto newBooking = new BookingDto(1, LocalDateTime.now(), LocalDateTime.now().plusDays(2),
                newItem, newUser, Status.WAITING);
        when(mockRepository.findById(1)).thenReturn(Optional.of(BookingMapper.toBooking(newBooking)));
        when(mockItemRepository.findById(1)).thenReturn(Optional.of(newItem));

        mockMvc.perform(get("/bookings/1")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.status", is("WAITING")));

        verify(mockRepository, times(1)).findById(1);

    }

    @Test
    public void getBookingTestNotFound() throws Exception {

        mockMvc.perform(get("/bookings/5")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getBookingTestUserMismatched() throws Exception {
        User newUser = new User(1, "Name", "email@email.com");
        Item newItem = new Item(1, "Name", "Description", true, newUser, null);

        BookingDto newBooking = new BookingDto(1, LocalDateTime.now(), LocalDateTime.now().plusDays(2),
                newItem, newUser, Status.WAITING);
        when(mockRepository.findById(1)).thenReturn(Optional.of(BookingMapper.toBooking(newBooking)));
        when(mockItemRepository.findById(1)).thenReturn(Optional.of(newItem));

        mockMvc.perform(get("/bookings/1")
                        .header("X-Sharer-User-Id", 2))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getAllBookingsForUserTest() throws Exception {
        User newUser = new User(1, "Name", "email@email.com");
        Item newItem = new Item(1, "Name", "Description", true, newUser, null);

        List<Booking> newBooking = Arrays.asList(
                new Booking(1, LocalDateTime.now(), LocalDateTime.now().plusDays(2),
                        newItem, newUser, Status.WAITING),
                new Booking(2, LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(3),
                        newItem, newUser, Status.WAITING));

        when(mockRepository.getBookingByBooker_Id(1, PageRequest.of(0, 10,
                Sort.by("start").descending())))
                .thenReturn(new PageImpl<>(newBooking));
        when(mockUserRepository.findById(anyInt())).thenReturn(Optional.of(newUser));

        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].status", is("WAITING")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[0].status", is("WAITING")));
        verify(mockRepository, times(1))
                .getBookingByBooker_Id(1, PageRequest.of(0, 10,
                        Sort.by("start").descending()));
    }

    @Test
    public void getAllBookingsForOwnerTest() throws Exception {
        User newUser = new User(1, "Name", "email@email.com");
        Item newItem = new Item(1, "Name", "Description", true, newUser, null);

        List<Booking> newBooking = Arrays.asList(
                new Booking(1, LocalDateTime.now(), LocalDateTime.now().plusDays(2),
                        newItem, newUser, Status.WAITING),
                new Booking(2, LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(3),
                        newItem, newUser, Status.WAITING));

        when(mockRepository.getBookingByOwner_Id(1, PageRequest.of(0, 10,
                Sort.by("start").descending())))
                .thenReturn(new PageImpl<>(newBooking));
        when(mockUserRepository.findById(anyInt())).thenReturn(Optional.of(newUser));

        mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].status", is("WAITING")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[0].status", is("WAITING")));
        verify(mockRepository, times(1))
                .getBookingByOwner_Id(1, PageRequest.of(0, 10,
                        Sort.by("start").descending()));
    }

    @Test
    public void updateBookingTestOK() throws Exception {
        User newUser = new User(1, "Name", "email@email.com");
        Item newItem = new Item(1, "Name", "Description", true, newUser, null);
        BookingDto newBooking = new BookingDto(1, LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2),
                newItem, newUser, Status.WAITING);
        when(mockUserRepository.findById(anyInt())).thenReturn(Optional.of(newUser));
        when(mockItemRepository.findById(anyInt())).thenReturn(Optional.of(newItem));
        when(mockRepository.findById(1)).thenReturn(Optional.of(BookingMapper.toBooking(newBooking)));
        when(mockRepository.save(any(Booking.class))).thenReturn(BookingMapper.toBooking(newBooking));
        mockMvc.perform(patch("/bookings/1")
                        .param("approved", String.valueOf(true))
                        .header("X-Sharer-User-Id", 1)
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(mockRepository, times(3)).findById(1);
        verify(mockRepository, times(1)).save(any(Booking.class));
    }

    @Test
    public void updateBookingRejectedTestOK() throws Exception {
        User newUser = new User(1, "Name", "email@email.com");
        Item newItem = new Item(1, "Name", "Description", true, newUser, null);
        BookingDto newBooking = new BookingDto(1, LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2),
                newItem, newUser, Status.WAITING);
        when(mockUserRepository.findById(anyInt())).thenReturn(Optional.of(newUser));
        when(mockItemRepository.findById(anyInt())).thenReturn(Optional.of(newItem));
        when(mockRepository.findById(1)).thenReturn(Optional.of(BookingMapper.toBooking(newBooking)));
        when(mockRepository.save(any(Booking.class))).thenReturn(BookingMapper.toBooking(newBooking));
        mockMvc.perform(patch("/bookings/1")
                        .param("approved", String.valueOf(false))
                        .header("X-Sharer-User-Id", 1)
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(mockRepository, times(3)).findById(1);
        verify(mockRepository, times(1)).save(any(Booking.class));
    }


    @Test
    public void updateBookingTestUserMismatch() throws Exception {
        User newUser = new User(1, "Name", "email@email.com");
        Item newItem = new Item(1, "Name", "Description", true, newUser, null);
        BookingDto newBooking = new BookingDto(1, LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2),
                newItem, newUser, Status.WAITING);
        when(mockRepository.findById(1)).thenReturn(Optional.of(BookingMapper.toBooking(newBooking)));
        mockMvc.perform(patch("/bookings/1")
                        .param("approved", String.valueOf(true))
                        .header("X-Sharer-User-Id", 2)
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        verify(mockRepository, times(1)).findById(1);
    }

    @Test
    public void updateBookingTestAlreadyApproved() throws Exception {
        User newUser = new User(1, "Name", "email@email.com");
        Item newItem = new Item(1, "Name", "Description", true, newUser, null);
        BookingDto newBooking = new BookingDto(1, LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2),
                newItem, newUser, Status.APPROVED);
        when(mockRepository.findById(1)).thenReturn(Optional.of(BookingMapper.toBooking(newBooking)));
        mockMvc.perform(patch("/bookings/1")
                        .param("approved", String.valueOf(true))
                        .header("X-Sharer-User-Id", 1)
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        verify(mockRepository, times(2)).findById(1);
    }

    @Test
    public void bookingCreateTestOK() throws Exception {
        User newUser = new User(2, "Name", "email@email.com");
        Item newItem = new Item(1, "Name", "Description", true, newUser, null);
        BookingDto newBooking = new BookingDto(1, LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2),
                newItem, null, Status.WAITING);
        when(mockRepository.save(any(Booking.class))).thenReturn(BookingMapper.toBooking(newBooking));
        when(mockUserRepository.findById(anyInt())).thenReturn(Optional.of(newUser));
        when(mockItemRepository.findById(anyInt())).thenReturn(Optional.of(newItem));
        mockMvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(newBooking))
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.status", is("WAITING")));
        verify(mockRepository, times(1)).save(any(Booking.class));
    }

    @Test
    public void bookingCreateTestNotAvailable() throws Exception {
        User newUser = new User(2, "Name", "email@email.com");
        Item newItem = new Item(1, "Name", "Description", false, newUser, null);
        BookingDto newBooking = new BookingDto(1, LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2),
                newItem, null, Status.WAITING);
        when(mockItemRepository.findById(anyInt())).thenReturn(Optional.of(newItem));
        mockMvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(newBooking))
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void bookingCreateTestUserMismatched() throws Exception {
        User newUser = new User(1, "Name", "email@email.com");
        Item newItem = new Item(1, "Name", "Description", true, newUser, null);
        BookingDto newBooking = new BookingDto(1, LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2),
                newItem, null, Status.WAITING);
        when(mockRepository.save(any(Booking.class))).thenReturn(BookingMapper.toBooking(newBooking));
        when(mockUserRepository.findById(anyInt())).thenReturn(Optional.of(newUser));
        when(mockItemRepository.findById(anyInt())).thenReturn(Optional.of(newItem));
        mockMvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(newBooking))
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isNotFound());
    }


}
