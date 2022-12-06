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
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.item.CommentRepository;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ItemControllerTests {

    private static final ObjectMapper om = //new ObjectMapper();
            JsonMapper.builder()
                    .addModule(new JavaTimeModule())
                    .build();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemRepository mockRepository;

    @MockBean
    private UserRepository mockUserRepository;

    @MockBean
    private CommentRepository mockCommentRepository;

    @MockBean
    private ItemRequestRepository mockRequestRepository;

    @MockBean
    private BookingRepository mockBookingRepository;
//        @BeforeAll
//        public static void init() {
////            Book book = new Book(1L, "Book Name", "Mkyong", new BigDecimal("9.99"));
////            when(mockRepository.findById(1L)).thenReturn(Optional.of(book));
//            ItemDto newItem = new ItemDto(1, "Name", "email@email.com");
//            when(mockRepository.findById(1)).thenReturn(Optional.of(ItemMapper.toItem(newItem)));
//        }

    @Test
    public void getItemTest() throws Exception {
        ItemDto newItem = new ItemDto(1, "Name", "Description", true, null, 0,
                null, null, null);
        when(mockRepository.findById(1)).thenReturn(Optional.of(ItemMapper.toItem(newItem, null)));

        mockMvc.perform(get("/items/1")
                        .header("X-Sharer-User-Id", 1))
                // .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Name")))
                .andExpect(jsonPath("$.description", is("Description")))
                .andExpect(jsonPath("$.available", is(true)));

        verify(mockRepository, times(2)).findById(1);

    }

    @Test
    public void getAllItemsTest() throws Exception {

        List<Item> items = Arrays.asList(
                new Item(1, "Name", "Description", true, null, null),
                new Item(2, "2Name", "2Description", true, null, null));

        when(mockRepository.getItemByOwner_Id(1, PageRequest.of(0, 10))).thenReturn(new PageImpl<>(items));

        mockMvc.perform(get("/items")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Name")))
                .andExpect(jsonPath("$[0].description", is("Description")))
                .andExpect(jsonPath("$[0].available", is(true)))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("2Name")))
                .andExpect(jsonPath("$[1].description", is("2Description")))
                .andExpect(jsonPath("$[1].available", is(true)));

        verify(mockRepository, times(1)).getItemByOwner_Id(1, PageRequest.of(0, 10));
    }

//    @Test
//    public void searchItemsTest() throws Exception {
//
//        List<Item> items = Arrays.asList(
//                new Item(1, "Name", "Description", true, null, null),
//                new Item(2, "Drill Bosch", "Professional", true, null, null));
//        String query = "Bosch";
//        when(mockRepository.search(query, PageRequest.of(0, 10))).thenReturn(new PageImpl<>(items));
//
//        mockMvc.perform(get("/items/search")
//                                .param("text", query)
////                        .header("X-Sharer-User-Id",1)
//                )
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
//        ;
//        verify(mockRepository, times(1)).search(query, PageRequest.of(0, 10));
//    }

 @Test
    public void searchItemsTestEmpty() throws Exception {

        List<Item> items = Arrays.asList(
                new Item(1, "Name", "Description", true, null, null),
                new Item(2, "Drill Bosch", "Professional", true, null, null));
        String query = "";
        when(mockRepository.search(query, PageRequest.of(0, 10))).thenReturn(new PageImpl<>(items));

        mockMvc.perform(get("/items/search")
                                .param("text", query)
//                        .header("X-Sharer-User-Id",1)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)))

//                  .andExpect(jsonPath("$[1].id", is(2)))
//                .andExpect(jsonPath("$[1].name", is("2Name")))
//                .andExpect(jsonPath("$[1].description", is("2Description")))
//                .andExpect(jsonPath("$[1].available", is(true)));
        ;
    }

    @Test
    public void updateItemTestOK() throws Exception {
        User newUser = new User(1, "Name", "email@email.com");
        ItemDto newItem = new ItemDto(1, "Name", "Description", true, newUser, 0,
                null, null, null);

        when(mockRepository.findById(1)).thenReturn(Optional.of(ItemMapper.toItem(newItem, null)));

        when(mockRepository.save(any(Item.class))).thenReturn(new Item());
        String patchInJson = "{\"name\":\"newname\"}";

        mockMvc.perform(patch("/items/1")
                        .content(patchInJson)
                        .header("X-Sharer-User-Id", 1)
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(mockRepository, times(2)).findById(1);
        verify(mockRepository, times(1)).save(any(Item.class));
    }


    @Test
    public void updateItemTestUserMismatched() throws Exception {
        User newUser = new User(1, "Name", "email@email.com");
        ItemDto newItem = new ItemDto(1, "Name", "Description", true, newUser, 0,
                null, null, null);

        when(mockRepository.findById(1)).thenReturn(Optional.of(ItemMapper.toItem(newItem, null)));

        when(mockRepository.save(any(Item.class))).thenReturn(new Item());
        String patchInJson = "{\"name\":\"newname\"}";

        mockMvc.perform(patch("/items/1")
                        .content(patchInJson)
                        .header("X-Sharer-User-Id", 2)
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(mockRepository, times(1)).findById(1);
    }

    @Test
    public void itemCreateTestOK() throws Exception {
        ItemDto newItem = new ItemDto(1, "Name", "Description", true, null, 0,
                null, null, null);
        User newUser = new User(1, "Name", "email@email.com");
        when(mockRepository.save(any(Item.class))).thenReturn(ItemMapper.toItem(newItem, null));
        when(mockUserRepository.findById(anyInt())).thenReturn(Optional.of(newUser));
        mockMvc.perform(post("/items")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(om.writeValueAsString(newItem))
                                .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Name")))
                .andExpect(jsonPath("$.description", is("Description")))
                .andExpect(jsonPath("$.available", is(true)));
        verify(mockRepository, times(1)).save(any(Item.class));
    }
    @Test
    public void itemCreateTestRequestNotFound() throws Exception {
        User newUser = new User(1, "Name", "email@email.com");
//        ItemRequest itemRequest = new ItemRequest(2, "Description", newUser, LocalDateTime.now());
        ItemDto newItem = new ItemDto(1, "Name", "Description", true, null, 1,
                null, null, null);
        when(mockRepository.save(any(Item.class))).thenReturn(ItemMapper.toItem(newItem, null));
        when(mockRequestRepository.findById(anyInt())).thenReturn(Optional.ofNullable(null));
        when(mockUserRepository.findById(anyInt())).thenReturn(Optional.of(newUser));
        mockMvc.perform(post("/items")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(om.writeValueAsString(newItem))
                                .header("X-Sharer-User-Id", 1))
                .andExpect(status().isNotFound());
    }

    @Test
    public void commentCreateTestOK() throws Exception {
        User newUser = new User(1, "Name", "email@email.com");
        Item newItem = new Item(1, "Name", "Description", true, newUser, null);
        List<Booking> booking = Arrays.asList(
                new Booking(1, LocalDateTime.now().minusDays(5), LocalDateTime.now().minusDays(1),
                        newItem, newUser, Status.APPROVED),
                new Booking(2, LocalDateTime.now().minusDays(7), LocalDateTime.now().minusDays(4),
                        newItem, newUser, Status.APPROVED));
        Comment newComment = new Comment(1, "Text", newItem, newUser, LocalDateTime.now());
        when(mockCommentRepository.save(any(Comment.class))).thenReturn(newComment);
        when(mockBookingRepository.getBookingsByItem_IdAndBooker_IdAndStatus_ApprovedIs(anyInt(),
                anyInt(), any())).thenReturn(booking);
        when(mockRepository.findById(anyInt())).thenReturn(Optional.of(newItem));
        when(mockUserRepository.findById(anyInt())).thenReturn(Optional.of(newUser));
        mockMvc.perform(post("/items/1/comment")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(om.writeValueAsString(newComment))
                                .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.text", is("Text")));
        verify(mockCommentRepository, times(1)).save(any(Comment.class));
    }

    @Test
    public void commentCreateTestWrongUser() throws Exception {
        User newUser = new User(1, "Name", "email@email.com");
        Item newItem = new Item(1, "Name", "Description", true, newUser, null);
//        List<Booking> booking = Arrays.asList(
//                new Booking(1, LocalDateTime.now().minusDays(5), LocalDateTime.now().minusDays(1),
//                        newItem, newUser, Status.APPROVED),
//                new Booking(2, LocalDateTime.now().minusDays(7), LocalDateTime.now().minusDays(4),
//                        newItem, newUser, Status.APPROVED));
        Comment newComment = new Comment(1, "Text", newItem, newUser, LocalDateTime.now());
        when(mockCommentRepository.save(any(Comment.class))).thenReturn(newComment);
        when(mockBookingRepository.getBookingsByItem_IdAndBooker_IdAndStatus_ApprovedIs(anyInt(),
                anyInt(), any())).thenReturn(new ArrayList<>());
        when(mockRepository.findById(anyInt())).thenReturn(Optional.of(newItem));
        when(mockUserRepository.findById(anyInt())).thenReturn(Optional.of(newUser));
        mockMvc.perform(post("/items/1/comment")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(om.writeValueAsString(newComment))
                                .header("X-Sharer-User-Id", 1))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void deleteItemTest() throws Exception {

        doNothing().when(mockRepository).deleteById(1);

        mockMvc.perform(delete("/items/1"))
                /*.andDo(print())*/
                .andExpect(status().isOk());

        verify(mockRepository, times(1)).deleteById(1);
    }

}
