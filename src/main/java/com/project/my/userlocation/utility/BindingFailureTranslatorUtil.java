package com.project.my.userlocation.utility;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.BindingResult;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Utility class for parse messages of binding errors while receiving invalid parameters.
 *
 * @see org.springframework.validation.BindException
 * @see com.project.my.userlocation.exception.handler.ExceptionHandlerAdvice
 */
@Slf4j
public class BindingFailureTranslatorUtil {

    public static List<String> buildErrorReasonInCaseOfBindingFailure(BindingResult bindingResult) {
        if (log.isDebugEnabled())
            log.debug("binding failure with reason [{}]", bindingResult.getFieldErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.joining(", ")));
        return bindingResult.getFieldErrors().stream()
                .map(br -> {
                    List<Object> args = Arrays.stream(Objects.requireNonNull(br.getArguments())).collect(Collectors.toList());
                    args.remove(0);
                    return MessageTranslatorUtil.getText(br.getDefaultMessage(), args.toArray(new Object[0]));
                })
                .collect(Collectors.toList());
    }
}