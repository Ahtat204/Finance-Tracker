package org.asue24.financetrackerbackend.security;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.asue24.financetrackerbackend.dto.UserRequestDto;
import org.asue24.financetrackerbackend.services.jwt.JwtService;
import org.asue24.financetrackerbackend.services.user.UserDetailsService;
import org.asue24.financetrackerbackend.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

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
     * @param request
     * @return
     * @throws ServletException
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        var path = request.getRequestURI();
        return path.startsWith("/api/auth");
    }

    /**
     * @param request
     * @param response
     * @param filterChain
     * @throws ServletException
     * @throws IOException
     */
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
        }
        filterChain.doFilter(request, response);
    }
}
