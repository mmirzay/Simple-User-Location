package com.project.my.userlocation.service;

import com.project.my.userlocation.entity.Location;
import com.project.my.userlocation.entity.User;
import com.project.my.userlocation.repository.LocationRepository;
import com.project.my.userlocation.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
class LocationServiceTest {

    @Autowired
    private LocationService service;

    @Autowired
    private UserService userService;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private UserRepository userRepository;


    @BeforeEach
    void beforeEach() {
        locationRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("A new user is added and a Location created for it. Then location is added and its value is asserted.")
    void givenAUserAndNewLocation_whenAddIt_thenItMustBeAdded() {
        User user = saveAndGetNewUser();
        Date createdOn = new Date();
        Location newLocation = getNewLocationForUser(user, createdOn);

        Location addedLocation = service.add(newLocation);

        assertLocationEquality(addedLocation, newLocation);
    }

    @Test
    @DisplayName("A location is unique by its user and createdOn date. Add the duplicate location, must result updating.")
    void givenAUserAndNewLocationWithSameCreatedOn_whenAddIt_thenItMustBeUpdated() {
        User user = saveAndGetNewUser();
        Date createdOn = new Date();
        Location newLocation = getNewLocationForUser(user, createdOn);
        Location addedLocation = service.add(newLocation);
        assertLocationEquality(addedLocation, newLocation);

        Location duplicateLocation = getDuplicateLocationForUser(user, createdOn);
        Location updatedLocation = service.add(duplicateLocation);

        assertLocationEquality(updatedLocation, duplicateLocation);
        assertEquals(1, locationRepository.count());
    }

    @Test
    @DisplayName("Three location added for user and last location of user is returned and asserted.")
    void givenAUserAndThreeLocations_whenGetLastLocation_thenItMustBeReturned() {
        User user = saveAndGetNewUser();
        Date lastDate = new Date();
        Date oneDayBefore = Date.from(lastDate.toInstant().minus(1, ChronoUnit.DAYS));
        Date twoDaysBefore = Date.from(lastDate.toInstant().minus(2, ChronoUnit.DAYS));
        Location lastDateLocation = getNewLocationForUser(user, lastDate);
        Location addedLastLocation = service.add(lastDateLocation);
        Location oneDayBeforeLocation = getNewLocationForUser(user, oneDayBefore);
        service.add(oneDayBeforeLocation);
        Location twoDaysBeforeLocation = getNewLocationForUser(user, twoDaysBefore);
        service.add(twoDaysBeforeLocation);
        assertEquals(3, locationRepository.count());

        Optional<Location> lastLocation = service.getLast(user.getId()).stream().findFirst();

        assertTrue(lastLocation.isPresent());
        assertLocationEquality(lastLocation.get(), addedLastLocation);
    }

    @Test
    @DisplayName("For a user which has no location, getting last location, return nothing.")
    void givenAUserWithNoLocationSaved_whenGetLastLocation_thenItMustReturnNothing() {
        User user = saveAndGetNewUser();

        Optional<Location> lastLocation = service.getLast(user.getId()).stream().findFirst();

        assertFalse(lastLocation.isPresent());
    }

    private void assertLocationEquality(Location addedLocation, Location newLocation) {
        assertNotNull(addedLocation.getUserId());
        assertNotNull(addedLocation.getCreatedOn());
        assertEquals(addedLocation.getUserId(), newLocation.getUserId());
        assertEquals(addedLocation.getCreatedOn().toInstant(), newLocation.getCreatedOn().toInstant());
        assertEquals(addedLocation.getLatitude(), newLocation.getLatitude());
        assertEquals(addedLocation.getLongitude(), newLocation.getLongitude());
    }

    private Location getNewLocationForUser(User user, Date createdOn) {
        return Location.builder()
                .id(Location.LocationId.builder()
                        .user(user)
                        .createdOn(createdOn)
                        .build())
                .latitude(52.25742342295784)
                .longitude(10.540583401747602)
                .build();
    }

    private Location getDuplicateLocationForUser(User user, Date createdOn) {
        return Location.builder()
                .id(Location.LocationId.builder()
                        .user(user)
                        .createdOn(createdOn)
                        .build())
                .latitude(53.25742342295784)
                .longitude(11.540583401747602)
                .build();
    }

    private User saveAndGetNewUser() {
        User user = User.builder().
                email("mirzay.mohsen@gmail.com")
                .firstName("mohsen")
                .secondName("mirzaei").
                build();
        return userService.addOrUpdate(user);
    }
}