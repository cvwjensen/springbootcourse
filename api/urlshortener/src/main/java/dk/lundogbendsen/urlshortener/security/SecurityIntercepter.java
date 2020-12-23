package dk.lundogbendsen.urlshortener.security;

import dk.lundogbendsen.urlshortener.model.User;
import dk.lundogbendsen.urlshortener.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Base64;

public class SecurityIntercepter implements HandlerInterceptor {
    @Autowired
    UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        final String auth = request.getHeader("Authorization");
        if (auth != null) {
            if (auth.startsWith("Basic")) {
                String userNamePassword = auth.substring(6);
                String decoded = new String(Base64.getDecoder().decode(userNamePassword));
                final String[] split = decoded.split(":");
                String userName = split[0];
                String password = split[1];
                final User user = userService.getUser(userName);
                if (user.getPassword().equals(password)) {
                    SecurityContext.setUser(user);
                } else {
                    return false;
                }
            } else if (auth.startsWith("Bearer")) {
                String protectToken = auth.substring(7);
                SecurityContext.setProtectToken(protectToken);
            } else {
                final String protectToken = request.getHeader("protectToken");
                if (protectToken != null) {
                    SecurityContext.setProtectToken(protectToken);
                }
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        SecurityContext.setUser(null);
        SecurityContext.setProtectToken(null);
    }
}
