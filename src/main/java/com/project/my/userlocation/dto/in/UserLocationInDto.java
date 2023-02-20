package com.project.my.userlocation.dto.in;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.project.my.userlocation.entity.Location;
import com.project.my.userlocation.utility.DateUtil;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.util.Date;

/**
 * Request body for {@link Location} as DTO when add location for a user.
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class UserLocationInDto {

    @NotEmpty(message = "in.userLocation.userId.notEmpty")
    private String userId;

    @NotNull(message = "in.userLocation.createdOn.notEmpty")
    @PastOrPresent(message = "in.userLocation.createdOn.notPastOrPresentDate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateUtil.DATE_TIME_PATTERN)
    private Date createdOn;

    @Valid
    @NotNull(message = "in.userLocation.location.notEmpty")
    private LocationInDto location;
}