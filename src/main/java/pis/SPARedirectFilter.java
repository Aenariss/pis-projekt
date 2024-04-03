/**
 * Filtering urls, forwarding to frontend if it is not an API call,
 * this is necessary to do because of client side routing (where is used react-router).
 * SPARedirectFilter.java
 * @author Lukáš Petr <xpetrl06>
 */
package pis;

import java.io.IOException;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;

@WebFilter(filterName = "spaRedirectFilter", urlPatterns = "/*")
public class SPARedirectFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        String path = req.getRequestURI().substring(req.getContextPath().length());

        // Check if the request is for a static file or an API call
        if (path.startsWith("/api/") || path.matches(".*\\.(html|js|css|jpg|png|gif)$")) {
            // Do not filter this requests, pass it down to other filters.
            chain.doFilter(request, response);
        }
        else {
            // Forward request to frontend.
            req.getRequestDispatcher("/index.html").forward(request, response);
        }
    }
}
