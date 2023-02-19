package com.project.my.userlocation.configuration;

import com.project.my.userlocation.utility.MessageTranslatorUtil;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

/**
 * Sets resources path for our messages using {@link MessageSource}
 * <br/>
 * Message resource will be used in {@link MessageTranslatorUtil}
 */
@Configuration
public class ResourceBundleConfig {

    public static final String CLASSPATH_MESSAGES = "classpath:messages/messages";

    @Bean
    public MessageSource messageSource() {
        var rs = new ReloadableResourceBundleMessageSource();
        rs.setBasenames(CLASSPATH_MESSAGES);
        rs.setDefaultEncoding("UTF-8");
        rs.setUseCodeAsDefaultMessage(true);
        MessageTranslatorUtil.setMessageSource(rs);
        return rs;
    }
}