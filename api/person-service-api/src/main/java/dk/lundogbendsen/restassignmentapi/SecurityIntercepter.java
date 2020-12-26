package dk.lundogbendsen.restassignmentapi;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SecurityIntercepter implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        final String apiToken = request.getHeader("apiToken");
        if (apiToken == null) {
            System.out.println("Unauthorized");
            throw new UnauthorizedException();
        }
        return true;
    }
}
