package com.project.my.userlocation.dto;

import com.project.my.userlocation.entity.User;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import static com.project.my.userlocation.configuration.Constant.EMAIL_MAX_LENGTH;
import static com.project.my.userlocation.configuration.Constant.EMAIL_PATTERN;

/**
 * Request body for {@link User} as DTO when add/update a user.
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class UserInDto {

    /**
     * If it is not set then new user is added. If it is set, will be used for update a user if existed.
     */
    private String userId;

    @NotEmpty(message = "in.user.email.notEmpty")
    @Size(max = EMAIL_MAX_LENGTH, message = "in.user.email.max.length.limit")
    @Email(regexp = EMAIL_PATTERN, message = "in.user.email.pattern.invalid")
    private String email;

    @NotEmpty(message = "in.user.firstName.notEmpty")
    private String firstName;

    @NotEmpty(message = "in.user.secondName.notEmpty")
    private String secondName;

    public User toUser() {
        User user = User.builder()
                .email(this.email)
                .firstName(this.firstName)
                .secondName(this.secondName)
                .build();
        user.setId(this.userId);
        return user;
    }
}
