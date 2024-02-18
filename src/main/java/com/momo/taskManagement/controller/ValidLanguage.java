package com.momo.taskManagement.controller;

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class ValidLanguage {
    private static final List<String> validLanguageList = Arrays.asList("en","ne");

    public static boolean isValidLanguage(String language){
        String languageCode = language.toLowerCase();
        return validLanguageList.contains(languageCode);
    }
}
