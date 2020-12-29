package dk.lundogbendsen.urlshortener.api;

import dk.lundogbendsen.urlshortener.service.TokenService;
import dk.lundogbendsen.urlshortener.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FollowControllerTest_S4 {
    @MockBean
    UserService userService;
    @MockBean
    TokenService tokenService;
    @Autowired
    FollowController followController;
    @Autowired
    TestRestTemplate restTemplate;

    @Test
    public void followToken() throws Exception {
        when(tokenService.resolveToken("abc", null)).thenReturn("https://dr.dk");
        final ResponseEntity<Void> forEntity = restTemplate.getForEntity("/abc", Void.class);
        assertEquals(HttpStatus.MOVED_PERMANENTLY, forEntity.getStatusCode());
        assertEquals("https://dr.dk", forEntity.getHeaders().getLocation().toString());
    }
}
