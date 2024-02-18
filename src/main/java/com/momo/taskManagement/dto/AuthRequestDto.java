package com.momo.taskManagement.dto;

public record AuthRequestDto (
    String firstName,
    String lastName,
    String email,
    String username,
    String password) {
}
