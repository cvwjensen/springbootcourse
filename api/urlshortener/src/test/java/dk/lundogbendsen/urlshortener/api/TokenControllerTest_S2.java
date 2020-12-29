package dk.lundogbendsen.urlshortener.api;

import dk.lundogbendsen.urlshortener.model.User;
import dk.lundogbendsen.urlshortener.service.TokenService;
import dk.lundogbendsen.urlshortener.service.UserService;
import dk.lundogbendsen.urlshortener.service.exceptions.TokenAlreadyExistsException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
class TokenControllerTest_S2 {
    @MockBean
    UserService userService;
    @MockBean
    TokenService tokenService;
    @Autowired TokenController tokenController;

    @Autowired MockMvc mvc;

    @BeforeEach
    public void setup() {
        when(userService.getUser("user1")).thenReturn(User.builder().username("user1").password("password1").build());
    }

    @Test
    public void createNonUniqueToken() throws Exception {
        when(tokenService.create(anyString(), anyString(), anyString(), anyString())).thenThrow(TokenAlreadyExistsException.class);

        Map<String, String> token = new HashMap<>();
        token.put("token", "abc");
        token.put("targetUrl", "https://dr.dk");
        token.put("protectToken", "protectAbc");
        JSONObject json = new JSONObject(token);

        mvc.perform(
                post("/token")
                        .contentType("application/json")
                        .content(json.toString())
                        .header("Authorization", "Basic " + Base64.getEncoder().encodeToString("user1:password1".getBytes(StandardCharsets.UTF_8))))
                .andExpect(status().is(HttpStatus.CONFLICT.value()))
        ;
    }
}