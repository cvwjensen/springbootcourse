package dk.lundogbendsen.urlshortener.service;

import dk.lundogbendsen.urlshortener.model.Token;
import dk.lundogbendsen.urlshortener.model.User;
import dk.lundogbendsen.urlshortener.service.exceptions.*;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TokenService {
    private HashMap<String, Token> tokens = new HashMap<>();


    public List<Token> listUserTokens(String userName) {
        final List<Token> userTokens = this.tokens.values().stream().filter(token -> token.getUserName().equals(userName)).collect(Collectors.toUnmodifiableList());
        return userTokens;
    }

    public void deleteTokens(User user) {
        tokens.values().removeIf(token -> token.getUserName().equals(user.getUsername()));
    }

    public Token create(String theToken, String targetUrl, String protectToken, String userName) {
        if (theToken.equals("token")) {
            throw new IllegalTokenNameException();
        }
        if (tokens.containsKey(theToken)) {
            throw new TokenAlreadyExistsException();
        }
        if (targetUrl == null) {
            throw new TokenTargetUrlIsNullException();
        }
        if (targetUrl.contains("localhost")) {
            throw new IllegalTargetUrlException();
        }
        try {
            new URI(targetUrl);
        } catch (URISyntaxException e) {
            throw new InvalidTargetUrlException();
        }

        final Token token = Token.builder().token(theToken).targetUrl(targetUrl).protectToken(protectToken).userName(userName).build();
        tokens.put(theToken, token);
        return token;
    }

    public Token update(String theToken, String targetUrl, String protectToken, String userName) {
        final Token token = tokens.get(theToken);
        if (token == null) {
            throw new TokenNotFoundExistsException();
        }
        if (!token.getUserName().equals(userName)) {
            throw new AccessDeniedException();
        }
        if (targetUrl == null) {
            targetUrl = token.getTargetUrl();
        }
        if (targetUrl.contains("localhost")) {
            throw new IllegalTargetUrlException();
        }
        try {
            new URI(targetUrl);
        } catch (URISyntaxException e) {
            throw new InvalidTargetUrlException();
        }
        token.setTargetUrl(targetUrl);
        token.setProtectToken(protectToken);
        return token;
    }

    public void deleteToken(String theToken, String userName) {
        final Token token = tokens.get(theToken);
        if (!token.getUserName().equals(userName)) {
            throw new AccessDeniedException();
        }
        tokens.remove(theToken);
    }

    public String resolveToken(String theToken, String protectToken) {
        final Token token = tokens.get(theToken);
        if (token == null) {
            throw new TokenNotFoundExistsException();
        }
        if (token.getProtectToken() != null && !token.getProtectToken().equals(protectToken)) {
            throw new AccessDeniedException();
        }

        return token.getTargetUrl();
    }

    public Token getToken(String theToken, String username) {
        final Token token = tokens.get(theToken);
        if (!token.getUserName().equals(username)) {
            throw new AccessDeniedException();
        }
        return token;
    }
}
