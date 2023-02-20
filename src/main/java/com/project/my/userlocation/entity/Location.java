package com.project.my.userlocation.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

/**
 * Indicates the model for Locations. It extended {@link UuidIdentifiedEntity} to get its UUID-based ID.<br/>
 * Every location are belonged to a user and also have a {@code createdOn} date.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "locations")
@Table(indexes = @Index(name = "uin_user_id_createdOn", columnList = "user_id, createdOn", unique = true))
public class Location extends UuidIdentifiedEntity {

    @ManyToOne(optional = false)
    private User user;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date createdOn;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;
}