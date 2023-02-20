package com.project.my.userlocation.dto.out;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * Response body for errors (e.g. exceptions). It includes a general title for different errors and reasons describing the error.
 * Also, the time which error is happened, will be shown in the response as {@code timestamp}.
 *
 * @see com.project.my.userlocation.exception.handler.ExceptionHandlerAdvice
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ErrorModel {

    @JsonProperty("timestamp")
    @Builder.Default
    private Date timestamp = new Date();

    @JsonProperty("errorTitle")
    private String title;

    @JsonProperty("errorReasons")
    private List<String> reasons;

}