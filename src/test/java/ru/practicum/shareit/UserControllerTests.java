package ru.practicum.shareit;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserDto;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserRepository;

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
public class UserControllerTests {

    private static final ObjectMapper om = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository mockRepository;

//        @BeforeAll
//        public static void init() {
////            Book book = new Book(1L, "Book Name", "Mkyong", new BigDecimal("9.99"));
////            when(mockRepository.findById(1L)).thenReturn(Optional.of(book));
//            UserDto newUser = new UserDto(1, "Name", "email@email.com");
//            when(mockRepository.findById(1)).thenReturn(Optional.of(UserMapper.toUser(newUser)));
//        }

    @Test
    public void getUserTest() throws Exception {
        UserDto newUser = new UserDto(1, "Name", "email@email.com");
        when(mockRepository.findById(1)).thenReturn(Optional.of(UserMapper.toUser(newUser)));

        mockMvc.perform(get("/users/1"))
                /*.andDo(print())*/
                // .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Name")))
                .andExpect(jsonPath("$.email", is("email@email.com")));

        verify(mockRepository, times(2)).findById(1);

    }

    @Test
    public void getAllUsersTest() throws Exception {

        List<User> users = Arrays.asList(
                new User(1, "Name", "email@email.com"),
                new User(2, "Name2", "email2@email.com"));

        when(mockRepository.findAll()).thenReturn(users);

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Name")))
                .andExpect(jsonPath("$[0].email", is("email@email.com")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("Name2")))
                .andExpect(jsonPath("$[1].email", is("email2@email.com")));

        verify(mockRepository, times(1)).findAll();
    }

    @Test
    public void updateUserTest() throws Exception {
        UserDto newUser = new UserDto(1, "Name", "email@email.com");
        when(mockRepository.findById(1)).thenReturn(Optional.of(UserMapper.toUser(newUser)));

        when(mockRepository.save(any(User.class))).thenReturn(new User());
        String patchInJson = "{\"name\":\"newname\"}";

        mockMvc.perform(patch("/users/1")
                        .content(patchInJson)
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(mockRepository, times(1)).findById(1);
        verify(mockRepository, times(1)).save(any(User.class));

    }

    @Test
    public void userCreateTest() throws Exception {

        UserDto newUser = new UserDto(1, "Name", "email@email.com");
        when(mockRepository.save(any(User.class))).thenReturn(UserMapper.toUser(newUser));

        mockMvc.perform(post("/users")
                        .content(om.writeValueAsString(newUser))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                /*.andDo(print())*/
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Name")))
                .andExpect(jsonPath("$.email", is("email@email.com")));

        verify(mockRepository, times(1)).save(any(User.class));

    }

    @Test
    public void deleteUserTest() throws Exception {

        doNothing().when(mockRepository).deleteById(1);

        mockMvc.perform(delete("/users/1"))
                /*.andDo(print())*/
                .andExpect(status().isOk());

        verify(mockRepository, times(1)).deleteById(1);
    }

}
