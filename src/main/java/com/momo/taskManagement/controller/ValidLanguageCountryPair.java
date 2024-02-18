package com.momo.taskManagement.controller;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ValidLanguageCountryPair {
    private static final Map<String,String> languageCountryCode = new HashMap<>();

    static {
        languageCountryCode.put("en","US");
        languageCountryCode.put("ne","NP");
    }
    public static boolean validateLanguageCountryPair(String language, String country){

        String tempLanguage = language.toLowerCase();
        String tempCountry = country.toUpperCase();

        if(languageCountryCode.containsKey(tempLanguage)){
            return languageCountryCode.get(tempLanguage).equals(tempCountry);
        }

        return false;
    }
}
