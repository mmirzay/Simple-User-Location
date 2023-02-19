package com.project.my.userlocation.utility;

import lombok.experimental.UtilityClass;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Locale;

/**
 * Utility methods to get messages from {@link MessageSource} defined in resource bundle.
 */
@UtilityClass
public class MessageTranslatorUtil {

    private static MessageSource messageSource;

    public static void setMessageSource(MessageSource messageSource) {
        MessageTranslatorUtil.messageSource = messageSource;
    }

    public static String getText(String msgCode) {
        Locale locale = LocaleContextHolder.getLocale();
        return getText(msgCode, locale);
    }

    public static String getText(String msgCode, Object... params) {
        Locale locale = LocaleContextHolder.getLocale();
        return getText(msgCode, locale, params);
    }

    static String getText(String msgCode, Locale locale, Object... params) {
        return messageSource.getMessage(msgCode, params, locale);
    }
}
