package com.project.my.userlocation.utility;

import lombok.extern.slf4j.Slf4j;
import org.postgresql.util.PSQLException;
import org.springframework.util.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class for parsing exception message of database (e.g. null value column, duplicate key value).
 *
 * @see PSQLException
 * @see com.project.my.userlocation.exception.handler.ExceptionHandlerAdvice
 */
@Slf4j
public class DbExceptionTranslatorUtil {

    private static final String NULL_CONSTRAINT_PREFIX = "ERROR: null value in column";
    private static final String UNIQUE_CONSTRAINT_PREFIX = "ERROR: duplicate key value";

    public static String findCause(PSQLException ex) {
        String cause;
        cause = checkUniqueConstraintViolation(ex.getMessage());
        if (StringUtils.hasText(cause)) return cause;
        cause = checkNullConstraintViolation(ex.getMessage());
        if (StringUtils.hasText(cause)) return cause;

        log.warn("could not extract psql exception msg [{}] ", ex.getMessage());
        return ex.getMessage();

    }

    private static String checkNullConstraintViolation(String msg) {
        String violationReason = "";
        if (msg.startsWith(NULL_CONSTRAINT_PREFIX)) {
            String fieldName = msg.split("\"")[1].replace("_", " ");
            violationReason = MessageTranslatorUtil.getText("exception.handler.DataBaseException.null.constraint.violation", fieldName);
        }
        return violationReason;
    }

    private static String checkUniqueConstraintViolation(String msg) {
        String violationReason = "";
        if (msg.startsWith(UNIQUE_CONSTRAINT_PREFIX)) {
            msg = msg.split("\"")[2];
            violationReason = MessageTranslatorUtil.getText("exception.handler.DataBaseException.unique.constraint.violation", getFields(msg));
        }
        return violationReason;
    }

    private static String[] getFields(String msg) {
        Pattern pattern = Pattern.compile("\\((.*?)\\)");
        Matcher matcher = pattern.matcher(msg);
        String[] messages = new String[2];
        int index = 0;
        while( matcher.find() ) {
            messages[index++] = matcher.group(1);
        }
        return messages;
    }
}