package com.project.my.userlocation.utility;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

/**
 * Utility class to parse exception message of http requests.
 *
 * @see org.springframework.http.converter.HttpMessageNotReadableException
 * @see com.project.my.userlocation.exception.handler.ExceptionHandlerAdvice
 */
@Slf4j
public class HttpFormatExceptionTranslatorUtil {

    private static final String EMPTY_REQUEST_BODY_PREFIX = "Required request body is missing";
    private static final String JSON_PARSE_ERROR_PREFIX = "JSON parse error:";
    private static final String JSON_PARSE_DATE_ERROR_PREFIX = "JSON parse error: Cannot deserialize value of type `java.util.Date`";
    private static final String JSON_PARSE_DOUBLE_ERROR_PREFIX = "JSON parse error: Cannot deserialize value of type `java.lang.Double`";

    public static String findCause(String message) {
        String cause;
        cause = checkEmptyResponseBodyViolation(message);
        if (StringUtils.hasText(cause)) return cause;
        cause = checkJsonParseViolation(message);
        if (StringUtils.hasText(cause)) return cause;

        log.warn("could not extract HttpMessageNotReadableException msg [{}] ", message);
        return message;

    }

    private static String checkEmptyResponseBodyViolation(String msg) {
        String violationReason = "";
        if (msg.startsWith(EMPTY_REQUEST_BODY_PREFIX)) {
            violationReason = MessageTranslatorUtil.getText("exception.handler.HttpMessageNotReadableException.emptyResponse.message");
        }
        return violationReason;
    }

    private static String checkJsonParseViolation(String msg) {
        String violationReason = "";
        if (msg.startsWith(JSON_PARSE_ERROR_PREFIX)) {
            String[] splitMessage = msg.split("\"");
            String errorParam = splitMessage[splitMessage.length - 2];
            if (msg.startsWith(JSON_PARSE_DATE_ERROR_PREFIX)) {
                violationReason = MessageTranslatorUtil
                        .getText("exception.handler.HttpMessageNotReadableException.jsonParseDateError.message",
                                errorParam, DateUtil.DATE_TIME_PATTERN);
            } else if (msg.startsWith(JSON_PARSE_DOUBLE_ERROR_PREFIX)) {
                violationReason = MessageTranslatorUtil
                        .getText("exception.handler.HttpMessageNotReadableException.jsonParseDoubleError.message",
                                errorParam);
            } else {
                violationReason = MessageTranslatorUtil
                        .getText("exception.handler.HttpMessageNotReadableException.jsonParseError.message",
                                errorParam);
            }
        }
        return violationReason;
    }
}
