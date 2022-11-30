package ru.practicum.shareit;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.item.ItemServiceImpl;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserDto;
import ru.practicum.shareit.user.UserService;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
//@Sql("/testschema.sql")
//@Sql("/testdata.sql")
class ItemServiceTests {
    private final ItemServiceImpl itemService;
    private final UserService userService;

//    @BeforeAll
//    public void prepareUser(){
//        UserDto user = new UserDto(1, "Ivanov", "ivan@ivan.iv");
//        userService.create(user);
//
//    }


//    @Test
//    public void testItemUpdate() {
//        Optional<User> userOptional = userStorage.getUser(1);
//        assertThat(userOptional)
//                .isPresent()
//                .hasValueSatisfying(user ->
//                        assertThat(user).hasFieldOrPropertyWithValue("id", 1)
//                );
//    }

//    @Test
//    public void testFindAllUsers() {
//        List<User> allUsers = userStorage.findAll();
//        assertEquals(3, allUsers.size());
//    }

    @Test
    public void testCreateItem() {
//        UserService mockUserService = Mockito.mock(UserService.class);
//        //bookService.setAuthorService(mockAuthorService);
//
//        Mockito
//                .when(mockAuthorService.getAuthorDescription(Mockito.anyInt()))
//                .thenReturn("знаменитый русский писатель");
//
//        String bookDescription = bookService.createBookDescription("Война и мир", 1898, 5, "Л.Н.Толстой");
//
//        Assertions.assertEquals("Война и мир, 1898 автор Л.Н.Толстой, знаменитый русский писатель", bookDescription);

        ItemDto item = new ItemDto();
        item.setName("Name");
        item.setDescription("Description");
        item.setAvailable(true);
        ItemDto savedItem = itemService.create(item,1);
        //savedUser.setId(userId);
        assertEquals(item, savedItem, "Users not equal");
    }

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
