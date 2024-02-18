package com.momo.taskManagement.controller;

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class ValidCountry {
    private static final List<String> validCountriesList = Arrays.asList("US","NP");

    public static boolean isValidCountry(String country){
        String countryCode = country.toUpperCase();
        return validCountriesList.contains(countryCode);
    }
}
