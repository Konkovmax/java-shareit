package ru.practicum.shareit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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
import java.util.Optional;

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

    private static final ObjectMapper om = //new ObjectMapper();
            JsonMapper.builder()
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
//        @BeforeAll
//        public static void init() {
////            Book book = new Book(1L, "Book Name", "Mkyong", new BigDecimal("9.99"));
////            when(mockRepository.findById(1L)).thenReturn(Optional.of(book));
//            ItemDto newItem = new ItemDto(1, "Name", "email@email.com");
//        }

    @Test
    public void getBookingTest() throws Exception {
        User newUser = new User(1, "Name", "email@email.com");
        Item newItem = new Item(1, "Name", "Description", true, newUser, null);

        BookingDto newBooking = new BookingDto(1, LocalDateTime.now(), LocalDateTime.now().plusDays(2),
                newItem, newUser, Status.WAITING);
        when(mockRepository.findById(1)).thenReturn(Optional.of(BookingMapper.toBooking(newBooking)));
        when(mockItemRepository.findById(1)).thenReturn(Optional.of(newItem));

        mockMvc.perform(get("/bookings/1")
                        .header("X-Sharer-User-Id", 1))
                // .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
//                .andExpect(jsonPath("$.owner", is(newUser)))
                .andExpect(jsonPath("$.status", is("WAITING")));

        verify(mockRepository, times(1)).findById(1);

    }

//    @Test
//    public void getAllItemsTest() throws Exception {
//        User newUser = new User(1, "Name", "email@email.com");
//        Item newItem = new Item(1, "Name", "Description", true, newUser, null);
//
//        List<Booking> newBooking = Arrays.asList(
//                new Booking(1, LocalDateTime.now(),LocalDateTime.now().plusDays(2),
//                newItem,newUser, Status.WAITING),
//                new Booking(2, LocalDateTime.now().plusDays(1),LocalDateTime.now().plusDays(3),
//                newItem,newUser, Status.WAITING));
//
//        when(mockRepository.getBookingByBooker_Id(1, PageRequest.of(0,10)))
//                .thenReturn(new PageImpl<>(newBooking));
//                when(mockUserRepository.findById(anyInt())).thenReturn(Optional.of(newUser));
//
//        mockMvc.perform(get("/bookings")
//                        .header("X-Sharer-User-Id",1))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(2)))
//                .andExpect(jsonPath("$[0].id", is(1)))
//                .andExpect(jsonPath("$[0].name", is("Name")))
//                .andExpect(jsonPath("$[0].description", is("Description")))
//                .andExpect(jsonPath("$[0].available", is(true)))
//                  .andExpect(jsonPath("$[1].id", is(2)))
//                .andExpect(jsonPath("$[1].name", is("2Name")))
//                .andExpect(jsonPath("$[1].description", is("2Description")))
//                .andExpect(jsonPath("$[1].available", is(true)));
//
//        verify(mockRepository, times(1))
//                .getBookingByBooker_Id(1, PageRequest.of(0,10));
//    }

//    @Test
//    public void searchItemsTest() throws Exception {
//
//        List<Item> items = Arrays.asList(
//                 new Item(1, "Name", "Description", true,null,null),
//                 new Item(2, "Drill Bosch", "Professional", true,null,null));
//String query = "Bosch";
//        when(mockRepository.search(query, PageRequest.of(0,10))).thenReturn(new PageImpl<>(items));
//
//        mockMvc.perform(get("/items/search")
//                                .param("text", query)
////                        .header("X-Sharer-User-Id",1)
//                        )
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(2)))
//                .andExpect(jsonPath("$[0].id", is(1)))
//                .andExpect(jsonPath("$[0].name", is("Drill Bosch")))
//                .andExpect(jsonPath("$[0].description", is("Professional")))
//                .andExpect(jsonPath("$[0].available", is(true)))
////                  .andExpect(jsonPath("$[1].id", is(2)))
////                .andExpect(jsonPath("$[1].name", is("2Name")))
////                .andExpect(jsonPath("$[1].description", is("2Description")))
////                .andExpect(jsonPath("$[1].available", is(true)));
//;
//        verify(mockRepository, times(1)).search(query, PageRequest.of(0,10));
//    }

    @Test
    public void updateBookingTest() throws Exception {
        User newUser = new User(1, "Name", "email@email.com");
        Item newItem = new Item(1, "Name", "Description", true, newUser, null);

        BookingDto newBooking = new BookingDto(1, LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2),
                newItem, newUser, Status.WAITING);
//        when(mockItemRepository.findById(1)).thenReturn(Optional.of(newItem));
        when(mockUserRepository.findById(anyInt())).thenReturn(Optional.of(newUser));
        when(mockItemRepository.findById(anyInt())).thenReturn(Optional.of(newItem));

        when(mockRepository.findById(1)).thenReturn(Optional.of(BookingMapper.toBooking(newBooking)));

        when(mockRepository.save(any(Booking.class))).thenReturn(BookingMapper.toBooking(newBooking));
//        String patchInJson = "{\"name\":\"newname\"}";

        mockMvc.perform(patch("/bookings/1")
                        .param("approved", String.valueOf(true))
                        .header("X-Sharer-User-Id", 1)
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(mockRepository, times(3)).findById(1);
        verify(mockRepository, times(1)).save(any(Booking.class));

    }

    @Test
    public void itemCreateTest() throws Exception {

        User newUser = new User(2, "Name", "email@email.com");
        Item newItem = new Item(1, "Name", "Description", true, newUser, null);

        BookingDto newBooking = new BookingDto(1, LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2),
                newItem, null, Status.WAITING);
//        when(mockItemRepository.findById(1)).thenReturn(Optional.of(newItem));
        when(mockRepository.save(any(Booking.class))).thenReturn(BookingMapper.toBooking(newBooking));
        when(mockUserRepository.findById(anyInt())).thenReturn(Optional.of(newUser));
        when(mockItemRepository.findById(anyInt())).thenReturn(Optional.of(newItem));

        mockMvc.perform(post("/bookings")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(om.writeValueAsString(newBooking))
//                        .contentType(MediaType.APPLICATION_JSON)
                                .header("X-Sharer-User-Id", 1)
//                , "text",
//                                "Content-Type", "application/json", "text",
//                                "Accept", "*/*", "text")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
//                .andExpect(jsonPath("$.name", is("Name")))
//                .andExpect(jsonPath("$.description", is("Description")))
                .andExpect(jsonPath("$.status", is("WAITING")));

        verify(mockRepository, times(1)).save(any(Booking.class));

    }


}
