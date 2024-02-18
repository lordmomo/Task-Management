package com.momo.taskManagement.service.interfaces;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Optional;

public interface AuthService {

    String login(String username, String password);

    void signUp(String firstName, String lastName, String email, String username, String password);

    Optional<String> getTokenFromRequest(HttpServletRequest request);

}
