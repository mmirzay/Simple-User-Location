package com.project.my.userlocation.dto.out;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.project.my.userlocation.entity.Location;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Response body for {@link Location} of a user.<br/>
 * Null value is not included in the output, so this DTO will be used for different endpoints' response.
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserLocationOutDto {

    private String userId;

    private String email;

    private String firstName;

    private String secondName;

    private LocationOutDto location;

    private List<LocationOutDto> locations;
}