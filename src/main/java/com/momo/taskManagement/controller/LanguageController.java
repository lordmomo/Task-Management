package com.momo.taskManagement.controller;

import com.momo.taskManagement.exception.InvalidCountryOrLanguageCodeException;
import com.momo.taskManagement.service.interfaces.LanguageService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@Slf4j
@RequestMapping("/api")
public class LanguageController {

    @Autowired
    LanguageService languageService;

    @PostMapping("/change-language")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public void changeLanguage(@RequestParam String language, @RequestParam String country,HttpServletResponse response) throws IOException {

//        try {
            languageService.changeLanguage(language, country);

            // Set language preference in the response header
            response.addHeader("X-Preferred-Language", language);
            log.info("X-Preferred language set in header.");

            // You might also want to include a success message or redirect the user
            // Sending Json response
            response.setContentType("application/json");
            response.getWriter().write("Language change successfully");
//        }
//        catch(InvalidCountryOrLanguageCodeException e){
//            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//            response.setContentType("application/json");
//            response.getWriter().write("Invalid country or language code !!!");
//        }
    }

}
