package com.momo.taskManagement.controller;

import com.momo.taskManagement.dto.AuthRequestDto;
import com.momo.taskManagement.dto.AuthResponseDto;
import com.momo.taskManagement.service.interfaces.AuthService;
import com.momo.taskManagement.service.interfaces.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicReference;

import static com.momo.taskManagement.controller.AuthStatus.NEW_REFRESH_TOKEN_CREATED_SUCCESSFULLY;
import static com.momo.taskManagement.controller.AuthStatus.NEW_REFRESH_TOKEN_CREATION_FAILED;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    @Autowired
    private final AuthService authServiceImpl;
    @Autowired
    private final JwtService jwtServiceImpl;


    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody AuthRequestDto authRequestDto) {

        try {
            var jwtToken = authServiceImpl.login(authRequestDto.username(), authRequestDto.password());

            var refreshToken = jwtServiceImpl.generateRefreshToken(authRequestDto.username());
            var authResponseDto = new AuthResponseDto(jwtToken, refreshToken, AuthStatus.LOGIN_SUCCESS);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(authResponseDto);
        } catch (Exception e) {
            var authResponseDto = new AuthResponseDto(null, null, AuthStatus.LOGIN_FAILED);

            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(authResponseDto);
        }
    }

    @PostMapping("/sign-up")
    public ResponseEntity<AuthStatus> signUp(@RequestBody AuthRequestDto authRequestDto) {

        try {

            authServiceImpl.signUp(authRequestDto.firstName(),
                    authRequestDto.lastName(), authRequestDto.email(),
                    authRequestDto.username(), authRequestDto.password());

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(AuthStatus.USER_CREATED_SUCCESSFULLY);

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(AuthStatus.USER_NOT_CREATED);
        }

    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthResponseDto> refreshToken(HttpServletRequest request) {
        AtomicReference<String> usernameHolder = new AtomicReference<>();
        var optionalToken = authServiceImpl.getTokenFromRequest(request);

        optionalToken.ifPresent(
                jwtToken -> {
                    if (jwtServiceImpl.validateToken(jwtToken)) {
                        jwtServiceImpl.getUsernameFromToken(jwtToken).ifPresent(
                                usernameHolder::set
                        );

                    } else {
                        System.out.println("Invalid token or token has been expired");
                    }
                });

        String username = usernameHolder.get();
        if (!username.isEmpty()) {
            var token = jwtServiceImpl.generateToken(username);
            var refreshToken = jwtServiceImpl.generateRefreshToken(username);

            var authResponseDto = new AuthResponseDto(token, refreshToken, NEW_REFRESH_TOKEN_CREATED_SUCCESSFULLY);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(authResponseDto);
        } else {
            System.out.println("Username not present or invalid token");

            var authResponseDto = new AuthResponseDto(null, null, NEW_REFRESH_TOKEN_CREATION_FAILED);

            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(authResponseDto);

        }

    }
}
