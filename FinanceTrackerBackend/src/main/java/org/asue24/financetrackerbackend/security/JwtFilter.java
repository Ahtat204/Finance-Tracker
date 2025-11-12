package org.asue24.financetrackerbackend.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.asue24.financetrackerbackend.dto.AuthDto;
import org.asue24.financetrackerbackend.services.jwt.JwtService;
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

    private final UserService userService;
    private final JwtService jwtService;

    @Autowired
    public JwtFilter(UserService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
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
        String token = null;
        String email = null;
        if (AuthHeader != null && AuthHeader.startsWith("Bearer ")) {
            token = AuthHeader.substring(7);
            email = jwtService.extractemail(token);
        }
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            var userinfo = userService.getUserByEmail(email);
            var user = new AuthDto(userinfo.getEmail(), userinfo.getPassword());
            if (jwtService.validateToken(token, user)) {
                var Authtoken = new UsernamePasswordAuthenticationToken(email, user.getPassword());
                Authtoken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(Authtoken);
            }
        }
        filterChain.doFilter(request, response);
    }
}
