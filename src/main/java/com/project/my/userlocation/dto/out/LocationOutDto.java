package com.project.my.userlocation.dto.out;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.project.my.userlocation.entity.Location;
import com.project.my.userlocation.utility.DateUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Response body for {@link Location} double values {@code latitude} and {@code longitude}.
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class LocationOutDto {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateUtil.DATE_TIME_PATTERN)
    private Date createdOn;

    private Double latitude;

    private Double longitude;
}
