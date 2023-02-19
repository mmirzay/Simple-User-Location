package com.project.my.userlocation.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.io.Serial;
import java.io.Serializable;

/**
 * Indicates the model for Users. It extended {@link UuidIdentifiedEntity} to get its UUID-based ID.
 *
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "users")
public class User extends UuidIdentifiedEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 6660626106360695253L;

    /**
     * Email for users is unique.
     */
    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String secondName;
}
