package com.example.jiraclone.security;

import com.example.jiraclone.service.impl.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            // 1. Get the JWT
            String token = jwtUtil.getTokenFromRequest(request);

            // 2. Validate
            if (StringUtils.hasText(token) && jwtUtil.validateToken(token)) {

                // --- THIS IS THE FIX ---

                // 3. Get EMAIL from the token (not User ID)
                String email = jwtUtil.getEmailFromToken(token); // <-- CALL THE CORRECT METHOD

                // 4. Load the user from the database using the email
                UserDetails userDetails = userDetailsService.loadUserByUsername(email);

                // 5. Create an authentication object
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // 6. Set the user in the SecurityContext
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            // This is where your error was logged
            logger.error("Cannot set user authentication:{}");
        }

        // 7. Continue the filter chain
        filterChain.doFilter(request, response);
    }
}