package dk.lundogbendsen.urlshortener.service;

import dk.lundogbendsen.urlshortener.model.User;
import dk.lundogbendsen.urlshortener.service.exceptions.UserExistsException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock TokenService tokenService;
    @Spy HashMap<String, User> users;
    @InjectMocks UserService userService;
    @Captor ArgumentCaptor<User> userArgumentCaptor;

    @Test
    public void createUniqueUserTest() {
        User user = userService.create("user1", "password1");
        assertEquals("user1", user.getUsername());
        assertEquals("password1", user.getPassword());
    }

    @Test
    public void createNonUniqueUserTest() {
        when(users.containsKey("user1")).thenReturn(true);
        try {
            User user = userService.create("user1", "password1");
            fail("A User with the same userName as an existing should throw an exception");
        } catch (UserExistsException e) {
        }
    }

    @Test
    public void deleteUserTest() {
        userService.create("user1", "password1");
        User user = userService.getUser("user1");
        assertNotNull(user);
        userService.delete("user1");
        User userAfterDelete = userService.getUser("user1");
        assertNull(userAfterDelete);

        verify(tokenService, times(1)).deleteTokens(any());
        verify(tokenService).deleteTokens(userArgumentCaptor.capture());
        assertEquals(user, userArgumentCaptor.getValue());

    }
}
