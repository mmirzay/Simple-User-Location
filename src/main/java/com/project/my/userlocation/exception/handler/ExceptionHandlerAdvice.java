package com.project.my.userlocation.exception.handler;

import com.project.my.userlocation.dto.out.ErrorModel;
import com.project.my.userlocation.exception.NotFoundException;
import com.project.my.userlocation.utility.BindingFailureTranslatorUtil;
import com.project.my.userlocation.utility.DbExceptionTranslatorUtil;
import com.project.my.userlocation.utility.MessageTranslatorUtil;
import lombok.extern.slf4j.Slf4j;
import org.postgresql.util.PSQLException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

/**
 * Global exception handler which maps exceptions to {@link ErrorModel}. Also, it responds {@code HttpStatus}.
 *
 * @see ErrorModel
 */
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class ExceptionHandlerAdvice {

    @ExceptionHandler({HttpMessageNotReadableException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorModel jsonFormatIsIncorrectException(HttpMessageNotReadableException ex) {
        log.error(ex.getMessage());
        return ErrorModel.builder()
                .title(MessageTranslatorUtil.getText("exception.handler.HttpMessageNotReadableException.title"))
                .reasons(List.of(MessageTranslatorUtil.getText("exception.handler.HttpMessageNotReadableException.message")))
                .build();
    }

    @ExceptionHandler({BindException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorModel handleBindingException(BindException ex) {
        log.error("received invalid parameter [{}] ", ex.getMessage());
        return ErrorModel.builder()
                .title(MessageTranslatorUtil.getText("exception.handler.BindException.title"))
                .reasons(BindingFailureTranslatorUtil.buildErrorReasonInCaseOfBindingFailure(ex.getBindingResult()))
                .build();
    }

    @ExceptionHandler({PSQLException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorModel handlingDbException(PSQLException ex) {
        log.error("database exception thrown : [{}]", ex.getMessage());
        return ErrorModel.builder()
                .title(MessageTranslatorUtil.getText("exception.handler.DataBaseException.title"))
                .reasons(List.of(DbExceptionTranslatorUtil.findCause(ex)))
                .build();
    }

    @ExceptionHandler({NotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorModel handlingNotFoundException(NotFoundException ex) {
        log.error("result not found : [{}]", ex.getMessage());
        return ErrorModel.builder()
                .title(MessageTranslatorUtil.getText("exception.handler.NotFoundException.title"))
                .reasons(List.of(ex.getMessage()))
                .build();
    }

}
