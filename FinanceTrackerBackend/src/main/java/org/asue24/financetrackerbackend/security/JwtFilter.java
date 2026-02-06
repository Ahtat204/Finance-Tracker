package org.asue24.financetrackerbackend.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.asue24.financetrackerbackend.dto.UserRequestDto;
import org.asue24.financetrackerbackend.services.jwt.JwtService;
import org.asue24.financetrackerbackend.services.user.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filter responsible for validating JWT
 * tokens on incoming HTTP requests.
 * * It intercepts requests, extracts the Bearer token,
 * validates it, and sets the * authentication in
 * the SecurityContext if the token is valid.
 */
@Order(2)
@Component
public class JwtFilter extends OncePerRequestFilter {
    private final UserDetailsService userService;
    private final JwtService jwtService;

    @Autowired
    public JwtFilter(UserDetailsService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    /**
     * /** * Determines whether the given request should be excluded from filtering.
     *  This implementation excludes all paths starting with "/api/auth".
     *  @param request The incoming HttpServletRequest.
     *  @return true if the request path starts with "/api/auth", false otherwise.
     *  @throws ServletException If an error occurs during the check.
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request)  {
        var path = request.getRequestURI();
        return path.startsWith("/api/auth");
    }


    /** * Processes the incoming request to check for a valid JWT token.
     *  If a valid token is found, the user details are loaded and the authentication
     *  is set in the Spring Security Context.
     *  @param request The current HTTP request.
     *  @param response The current HTTP response.
     *  @param filterChain The chain of remaining filters to execute.
     *  @throws ServletException If a servlet-related error occurs.
     *  @throws IOException If an I/O error occurs (e.g., writing the error response). */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var AuthHeader = request.getHeader("Authorization");
        if (AuthHeader == null || !AuthHeader.startsWith("Bearer ")) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authorization header must be provided in 'Bearer <token>' format.");
            return;
        }
        String token = null;
        String email = null;
        if (AuthHeader != null && AuthHeader.startsWith("Bearer ")) {
            token = AuthHeader.substring(7);
            email = jwtService.extractemail(token);
        }
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            var userinfo = userService.loadUserByUsername(email);
            var user = new UserRequestDto(userinfo.getUsername(), userinfo.getPassword());
            if (jwtService.validateToken(token, user)) {
                var Authtoken = new UsernamePasswordAuthenticationToken(userinfo, null, userinfo.getAuthorities());
                Authtoken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(Authtoken);

            }
            var id=request.getAttribute("id");
            if (id == null) {
                request.setAttribute("id", jwtService.extractemail(token));
            }
             // passes the userId to be used in the next Filter, which the IdBasedRateLimitingFilter
        }

        filterChain.doFilter(request, response);
    }
}
