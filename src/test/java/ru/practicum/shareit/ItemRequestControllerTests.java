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
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
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

    private static final ObjectMapper om = JsonMapper.builder()
            .addModule(new JavaTimeModule())
            .build();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemRequestRepository mockRepository;

    @MockBean
    private UserRepository mockUserRepository;

    @Test
    public void getItemRequestTest() throws Exception {
        User newUser = new User(1, "Name", "email@email.com");
        ItemRequestDto itemRequestDto = new ItemRequestDto(1, "Description", newUser, LocalDateTime.now(),
                null);
        when(mockRepository.findById(1)).thenReturn(Optional.of(ItemRequestMapper.toItemRequest(itemRequestDto)));
        when(mockUserRepository.findById(anyInt())).thenReturn(Optional.of(newUser));
        mockMvc.perform(get("/requests/1")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.description", is("Description")));

        verify(mockRepository, times(1)).findById(1);

    }

    @Test
    public void getAllRequestsTest() throws Exception {

        List<ItemRequest> items = Arrays.asList(
                new ItemRequest(1, "Description", null, LocalDateTime.now()),
                new ItemRequest(2, "2Description", null, LocalDateTime.now()));

        when(mockRepository.findItemRequestByRequester_IdNot(1, PageRequest.of(0, 10,
                Sort.by("created").descending()))).thenReturn(new PageImpl<>(items));

        mockMvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].description", is("Description")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].description", is("2Description")));

        verify(mockRepository, times(1)).findItemRequestByRequester_IdNot(1,
                PageRequest.of(0, 10, Sort.by("created").descending()));
    }

    @Test
    public void getAllOwnRequestsTest() throws Exception {

        List<ItemRequest> items = Arrays.asList(
                new ItemRequest(1, "Description", null, LocalDateTime.now()),
                new ItemRequest(2, "2Description", null, LocalDateTime.now()));
        when(mockUserRepository.findById(anyInt())).thenReturn(Optional.of(new User()));
        when(mockRepository.getItemRequestByRequester_Id(1)).thenReturn(items);

        mockMvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].description", is("Description")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].description", is("2Description")));

        verify(mockRepository, times(1)).getItemRequestByRequester_Id(1);
    }

    @Test
    public void itemRequestCreateTest() throws Exception {

        User newUser = new User(1, "Name", "email@email.com");
        ItemRequestDto newItemRequest = new ItemRequestDto(1, "Description", newUser, LocalDateTime.now(),
                null);
        when(mockRepository.save(any(ItemRequest.class))).thenReturn(ItemRequestMapper
                .toItemRequest(newItemRequest));
        when(mockUserRepository.findById(anyInt())).thenReturn(Optional.of(newUser));

        mockMvc.perform(post("/requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(newItemRequest))
                        .header("X-Sharer-User-Id", 1)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.description", is("Description")));

        verify(mockRepository, times(1)).save(any(ItemRequest.class));

    }
}
