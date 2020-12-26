package dk.lundogbendsen.restassignmentapi;

import org.springframework.http.HttpStatus;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SecurityFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        final String apiToken = req.getHeader("apiToken");
        if (apiToken == null) {
            System.out.println("Unauthorized");
            HttpServletResponse res = (HttpServletResponse) response;
            res.sendError(HttpStatus.UNAUTHORIZED.value(), "You shall not pass!");
            return;
        }

        chain.doFilter(request, response);
    }
}
