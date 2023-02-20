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
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

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

    private void assertLocationEquality(Location addedLocation, Location newLocation) {
        assertNotNull(addedLocation.getUser().getId());
        assertNotNull(addedLocation.getCreatedOn());
        assertEquals(addedLocation.getUser().getId(), newLocation.getUser().getId());
        assertEquals(addedLocation.getCreatedOn().toInstant(), newLocation.getCreatedOn().toInstant());
        assertEquals(addedLocation.getLatitude(), newLocation.getLatitude());
        assertEquals(addedLocation.getLongitude(), newLocation.getLongitude());
    }

    private Location getNewLocationForUser(User user, Date createdOn) {
        return Location.builder()
                .user(user)
                .createdOn(createdOn)
                .latitude(52.25742342295784)
                .longitude(10.540583401747602)
                .build();
    }

    private Location getDuplicateLocationForUser(User user, Date createdOn) {
        return Location.builder()
                .user(user)
                .createdOn(createdOn)
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