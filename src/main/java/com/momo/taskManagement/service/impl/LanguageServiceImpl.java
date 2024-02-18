package com.momo.taskManagement.service.impl;

import com.momo.taskManagement.exception.InvalidCountryOrLanguageCodeException;
import com.momo.taskManagement.service.interfaces.LanguageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import java.util.Locale;

import static com.momo.taskManagement.controller.ValidCountry.isValidCountry;
import static com.momo.taskManagement.controller.ValidLanguage.isValidLanguage;
import static com.momo.taskManagement.controller.ValidLanguageCountryPair.validateLanguageCountryPair;

@Service
@Slf4j
public class LanguageServiceImpl implements LanguageService {


    @Autowired
    ReloadableResourceBundleMessageSource messageSource;
    @Autowired
    AcceptHeaderLocaleResolver localeResolver;

    @Override
    public void changeLanguage(String language, String country) {

        if (!isValidLanguage(language) || !isValidCountry(country) ||
                        !validateLanguageCountryPair(language, country)
        ) {
            log.error("invalid language or country encountered.");
            throw new InvalidCountryOrLanguageCodeException("Invalid language or country code !!!");
        }

        messageSource.setBasename("classpath:messages_" + language + "_" + country);

        log.info("message basename set.");
        Locale locale = new Locale(language, country);
        localeResolver.setDefaultLocale(locale);

        log.info("Default locale set.");
//        classpath:messages_ne_NP

    }

}
