package com.momo.taskManagement.service.impl;

import com.momo.taskManagement.userFolder.model.Role;
import com.momo.taskManagement.userFolder.model.User;
import com.momo.taskManagement.userFolder.repository.UserRepository;
import com.momo.taskManagement.service.interfaces.AuthService;
import com.momo.taskManagement.service.interfaces.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Autowired
    private final JwtService jwtServiceImpl;

    @Override
    public String login(String username, String password) {
        var authToken = new UsernamePasswordAuthenticationToken(username, password);

        try {

            //It is the responsibility of authenticationManger
            //to call respective authentication provider to authenticate
            //these credentials
            var authenticate = authenticationManager.authenticate(authToken);
            return jwtServiceImpl.generateToken(((UserDetails) (authenticate.getPrincipal())).getUsername());

        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Invalid email or password");
        }

    }


    @Override
    public void signUp(String firstName, String lastName, String email, String username, String password) {
        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("User Already Exists");
        }

        //Encode password

        var encodedPassword = passwordEncoder.encode(password);

        Role role = new Role("2", "User");

        var user = User.builder()
                .firstName(firstName.toLowerCase())
                .lastName(lastName.toLowerCase())
                .email(email)
                .username(username)
                .password(encodedPassword)
                .role(role)
                .build();

        //Save user
        userRepository.save(user);

    }

    public Optional<String> getTokenFromRequest(HttpServletRequest request) {
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



