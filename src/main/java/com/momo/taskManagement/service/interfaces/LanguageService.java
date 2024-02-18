package com.momo.taskManagement.service.interfaces;

import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface LanguageService {
    void changeLanguage(String language, String country);
}
