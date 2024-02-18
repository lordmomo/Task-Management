package com.momo.taskManagement.security;

import com.momo.taskManagement.service.interfaces.JwtService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
public class JwtAuthenticateFilter extends OncePerRequestFilter {

    @Autowired
    JwtService jwtServiceImpl;

    @Autowired
    UserDetailsService userDetailsServiceImpl;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        var jwtTokenOptional = getTokenFromRequest(request);

        jwtTokenOptional.ifPresent(
                jwtToken -> {
                    if (jwtServiceImpl.validateToken(jwtToken)) {
                        try {
                            // Fetch user details with the help of username

                            var usernameOptional = jwtServiceImpl.getUsernameFromToken(jwtToken);
                            usernameOptional.ifPresent(username -> {
                                var userDetails = userDetailsServiceImpl.loadUserByUsername(username);//error --null return
                                System.out.println("2");
                                System.out.println(userDetails);
                                //Create Authentication token
                                var authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                                //Set authentication token to Security Context
                                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                            });

                        } catch (IllegalAccessError e) {
                            logger.info("Illegal Argument while fetching the username !!");
                            e.printStackTrace();
                        } catch (ExpiredJwtException e) {
                            logger.info("Given jwt token is expired !!");
                            e.printStackTrace();
                        } catch (MalformedJwtException e) {
                            logger.info("Some changed has done in token !! Invalid Token");
                            e.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                            System.out.println("User not found");
                        }

                    } else {
                        System.out.println("Token has been expired");
                    }
                }
        );
        filterChain.doFilter(request, response);
    }

    private Optional<String> getTokenFromRequest(HttpServletRequest request) {
        //Extract authentication header
        var authHeader = request.getHeader("Authorization");
        //Bearer <JWT TOKEN>
        if (authHeader != null && authHeader.startsWith("Bearer")) {
            return Optional.of(authHeader.substring(7));
        } else {
            return Optional.empty();
        }
    }
}
