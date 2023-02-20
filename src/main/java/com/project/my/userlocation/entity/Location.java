package com.project.my.userlocation.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * Indicates the model for Locations. Every location are belonged to a user and also have a {@code createdOn} date. <br/>
 * Note that Location has a composite ID including, {@code user_id} and {@code cratedOn} date to support index.
 *
 * @see User
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "locations")
public class Location implements Serializable {

    @Serial
    private static final long serialVersionUID = -4233884329216600450L;

    @EmbeddedId
    @Setter
    private LocationId id;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    @Embeddable
    @EqualsAndHashCode(onlyExplicitlyIncluded = true)
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class LocationId implements Serializable {
        @Serial
        private static final long serialVersionUID = -6580461804516405147L;

        @EqualsAndHashCode.Include
        @ManyToOne(optional = false)
        @JoinColumn(name = "user_id")
        private User user;

        @EqualsAndHashCode.Include
        @Temporal(TemporalType.TIMESTAMP)
        @Column(name = "created_on", nullable = false)
        private Date createdOn;
    }

    /**
     * To get user ID directly from composite ID.
     * @return user_id belong to the location
     */
    public String getUserId() {
        return this.getId().getUser().getId();
    }

    /**
     * To get createdOn date directly from composite ID.
     * @return createdOn date
     */
    public Date getCreatedOn() {
        return this.getId().getCreatedOn();
    }
}