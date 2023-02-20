package com.project.my.userlocation.dto.in;

import com.project.my.userlocation.entity.Location;
import lombok.*;

import javax.validation.constraints.NotNull;

/**
 * Request body for {@link Location} double values as {@code latitude} and {@code longitude}.
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class LocationInDto {
    @NotNull(message = "in.location.latitude.notEmpty")
    private Double latitude;

    @NotNull(message = "in.location.longitude.notEmpty")
    private Double longitude;
}