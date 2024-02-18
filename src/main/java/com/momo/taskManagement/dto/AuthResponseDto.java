package com.momo.taskManagement.dto;

import com.momo.taskManagement.controller.AuthStatus;

public record AuthResponseDto (String accessToken, String refreshToken, AuthStatus authStatus){
}
