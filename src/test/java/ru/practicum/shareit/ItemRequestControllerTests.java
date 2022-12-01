package ru.practicum.shareit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.ItemRequestMapper;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ItemRequestControllerTests {

    private static final ObjectMapper om = //new ObjectMapper();
            JsonMapper.builder()
                    .addModule(new JavaTimeModule())
                    .build();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemRequestRepository mockRepository;

    @MockBean
    private UserRepository mockUserRepository;

//    @MockBean
//    private CommentRepository mockCommentRepository;
//
//    @MockBean
//    private BookingRepository mockBookingRepository;
//        @BeforeAll
//        public static void init() {
////            Book book = new Book(1L, "Book Name", "Mkyong", new BigDecimal("9.99"));
////            when(mockRepository.findById(1L)).thenReturn(Optional.of(book));
//            ItemDto newItem = new ItemDto(1, "Name", "email@email.com");
//            when(mockRepository.findById(1)).thenReturn(Optional.of(ItemMapper.toItem(newItem)));
//        }

    @Test
    public void getItemRequestTest() throws Exception {
        User newUser = new User(1, "Name", "email@email.com");
        ItemRequestDto itemRequestDto = new ItemRequestDto(1, "Description", newUser, LocalDateTime.now(),
                null);
        when(mockRepository.findById(1)).thenReturn(Optional.of(ItemRequestMapper.toItemRequest(itemRequestDto)));
        when(mockUserRepository.findById(anyInt())).thenReturn(Optional.of(newUser));
        mockMvc.perform(get("/requests/1")
                        .header("X-Sharer-User-Id", 1))
                // .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.description", is("Description")));

        verify(mockRepository, times(1)).findById(1);

    }

//    @Test
//    public void getAllRequestsTest() throws Exception {
//
//        List<ItemRequest> items = Arrays.asList(
//                new ItemRequest(1, "Description", null, LocalDateTime.now()),
//                new ItemRequest(2, "2Description", null, LocalDateTime.now()));
//
//        when(mockRepository.findItemRequestByRequester_IdNot(1, PageRequest.of(0, 10))).thenReturn(new PageImpl<>(items));
//
//        mockMvc.perform(get("/requests/all")
//                        .header("X-Sharer-User-Id", 1))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(2)))
//                .andExpect(jsonPath("$[0].id", is(1)))
//                .andExpect(jsonPath("$[0].description", is("Description")))
//                .andExpect(jsonPath("$[1].id", is(2)))
//                .andExpect(jsonPath("$[1].description", is("2Description")));
//
//        verify(mockRepository, times(1)).findItemRequestByRequester_IdNot(1, PageRequest.of(0, 10));
//    }

    @Test
    public void itemRequestCreateTest() throws Exception {

        User newUser = new User(1, "Name", "email@email.com");
        ItemRequestDto newItemRequest = new ItemRequestDto(1, "Description", newUser, LocalDateTime.now(),
                null);
//        ItemDto newItem = new ItemDto(1, "Name", "Description", true,null,0,
//                null,null,null);
        when(mockRepository.save(any(ItemRequest.class))).thenReturn(ItemRequestMapper
                .toItemRequest(newItemRequest));
        when(mockUserRepository.findById(anyInt())).thenReturn(Optional.of(newUser));

        mockMvc.perform(post("/requests")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(om.writeValueAsString(newItemRequest))
//                        .contentType(MediaType.APPLICATION_JSON)
                                .header("X-Sharer-User-Id", 1)
//                , "text",
//                                "Content-Type", "application/json", "text",
//                                "Accept", "*/*", "text")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.description", is("Description")));

        verify(mockRepository, times(1)).save(any(ItemRequest.class));

    }
}
