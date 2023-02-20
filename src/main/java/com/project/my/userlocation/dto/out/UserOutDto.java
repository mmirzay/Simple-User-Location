package com.project.my.userlocation.dto.out;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.project.my.userlocation.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Response body for {@link User} as DTO when add/update a user.
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class UserOutDto {

    private String userId;

    private String email;

    private String firstName;

    private String secondName;
}