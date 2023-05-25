package com.student.northwind.util.component;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Locale;

@Component
@RequiredArgsConstructor
public class MessageResourceComponent {
    private final MessageSource messageSource;
    private final HttpServletRequest request;

    private static final Locale LOCALE_DEFAULT = Locale.forLanguageTag("vi-VN");
    private static final String LANG_KEY = "lang";

    public String getMessage(String key) {
        return messageSource.getMessage(key, null, key, getLocale(request));
    }

    public String getMessageVN(String code, String[] arg) {
        return this.messageSource.getMessage(code, arg, LOCALE_DEFAULT);
    }

    public String getMessageVN(String code) {
        return this.messageSource.getMessage(code, null, LOCALE_DEFAULT);
    }

    public String getMessage(String key, String defaultMessage) {
        return messageSource.getMessage(key, null, defaultMessage, getLocale(request));
    }

    public String getMessage(String key, String[] arg) {
        if (arg == null || arg.length == 0) {
            return getMessage(key);
        }
        return messageSource.getMessage(key, arg, key, getLocale(request));
    }

    public String getMessage(String key, List<String> arg) {
        if (arg == null || arg.size() == 0) {
            return getMessage(key);
        }
        return messageSource.getMessage(key, arg.toArray(), key, getLocale(request));
    }

    private Locale getLocale(HttpServletRequest request) {
        if (request == null) {
            return LOCALE_DEFAULT;
        }
        String headerLang = request.getHeader(LANG_KEY);
        if (StringUtils.isNotBlank(headerLang)) {
            return Locale.forLanguageTag(headerLang);
        }

        String paramLang = request.getParameter(LANG_KEY);
        if (StringUtils.isNotBlank(paramLang)) {
            return Locale.forLanguageTag(paramLang);
        }

        return LOCALE_DEFAULT;
    }

    public String getLocaleDefaultMessage(String key, String[] args) {
        return messageSource.getMessage(key, args, key, LOCALE_DEFAULT);
    }
}
