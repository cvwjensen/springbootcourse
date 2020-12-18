package dk.lundogbendsen.logging;

import org.slf4j.MDC;

import javax.servlet.*;
import java.io.IOException;
import java.util.UUID;

public class TracingFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        MDC.put("TraceId", UUID.randomUUID().toString());
        filterChain.doFilter(servletRequest,servletResponse);
        MDC.remove("TraceId");
    }
}
