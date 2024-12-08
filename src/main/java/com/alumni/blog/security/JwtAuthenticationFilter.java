package com.alumni.blog.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenHelper jwtTokenHelper;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        // Get the Authorization header
        String requestTokenHeader = request.getHeader("Authorization");

        String username = null;
        String jwtToken = null;

        // Check if the header contains a Bearer token
        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer")) {
            jwtToken = requestTokenHeader.substring(7); // Remove "Bearer " prefix
            try {
                username = jwtTokenHelper.getUserNameFromToken(jwtToken);
            } catch (Exception e) {
                System.out.println("Invalid JWT Token: " + e.getMessage());
            }
        } else {
            System.out.println("JWT Token does not begin with Bearer String");
        }

        // If the token is valid, load the user details and set authentication
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if (jwtTokenHelper.validateToken(jwtToken, userDetails)) {
                // Use UsernamePasswordAuthenticationToken for setting authentication
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Set authentication in the context
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }

        // Proceed with the filter chain
        chain.doFilter(request, response);
    }
}