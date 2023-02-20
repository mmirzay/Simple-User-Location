package com.project.my.userlocation.service;

import com.project.my.userlocation.entity.Location;
import com.project.my.userlocation.entity.User;
import com.project.my.userlocation.exception.NotFoundException;
import com.project.my.userlocation.repository.LocationRepository;
import com.project.my.userlocation.repository.UserRepository;
import com.project.my.userlocation.utility.MessageTranslatorUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService service;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LocationRepository locationRepository;


    @BeforeEach
    void beforeEach() {
        locationRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("A new user data is created. Then User is added. Finally new user values are asserted.")
    void givenANewUser_whenAddOrUpdateIt_thenItMustBeAdded() {
        User newUser = getNewUser();

        User addedUser = service.addOrUpdate(newUser);

        assertUserEquality(addedUser, newUser);
    }

    @Test
    @DisplayName("A new user data is created with null values. Then adding it must throw exception.")
    void givenANewUserWithNullValues_whenAddOrUpdateIt_thenItMustThrowException() {
        User userWithNullValues = getUserWithNullValues();

        assertThrows(DataIntegrityViolationException.class, () -> service.addOrUpdate(userWithNullValues));
    }

    @Test
    @DisplayName("A new user with a duplicate email is adding and must throw exception because email is unique for users.")
    void givenANewUserWithDuplicateEmail_whenAddOrUpdateIt_thenItMustThrowException() {
        User userWithDuplicateEmail = getNewUserWithDuplicateEmail();

        assertThrows(DataIntegrityViolationException.class, () -> service.addOrUpdate(userWithDuplicateEmail));
    }

    @Test
    @DisplayName("First and Second names of existing user are updated. Finally updated values are asserted.")
    void givenAnExistUser_whenUpdateItNames_thenItMustBeUpdated() {
        User toUpdate = getUpdatingValues();

        User updatedUser = service.addOrUpdate(toUpdate);

        assertUserEquality(updatedUser, toUpdate);
    }

    @Test
    @DisplayName("A user with invalid ID is updated. Then a NotFoundException is thrown.")
    void givenAUserWithInvalidId_whenUpdateIt_thenItMustThrowException() {
        User userWithInvalidId = getUpdatingValues();
        userWithInvalidId.setId("InvalidId");

        Exception notFoundException = assertThrows(Exception.class, () -> service.addOrUpdate(userWithInvalidId));
        assertEquals(notFoundException.getMessage(), MessageTranslatorUtil.getText("service.user.get.notFound"));
    }

    @Test
    @DisplayName("A new user created and using its ID, new Location will be added for it.")
    void givenAnExistUser_whenAddNewLocation_thenItMustBeAdded() {
        String userId = saveNewUserAndGetUserId();
        Location location = getNewLocationWithCreatedOn(new Date());

        Location addedLocation = service.addLocationForUser(userId, location);

        assertNotNull(addedLocation.getUserId());
        assertEquals(addedLocation.getUserId(), userId);
        assertEquals(addedLocation.getCreatedOn(), location.getCreatedOn());
        assertEquals(addedLocation.getLatitude(), location.getLatitude());
        assertEquals(addedLocation.getLongitude(), location.getLongitude());
    }

    @Test
    @DisplayName("An Invalid user ID is used to add a new location. Then a NotFoundException is thrown.")
    void givenAUserWithInvalidId_whenAddNewLocation_thenItMustThrowException() {
        String invalidUserId = "Invalid user ID";
        Location location = getNewLocationWithCreatedOn(new Date());

        NotFoundException notFoundException = assertThrows(NotFoundException.class,
                () -> service.addLocationForUser(invalidUserId, location));

        assertEquals(notFoundException.getMessage(), MessageTranslatorUtil.getText("service.user.get.notFound"));
    }

    @Test
    @DisplayName("A duplicate location with same createdOn added. Then must update the location.")
    void givenAUserAndExistLocationWithDuplicateCreatedOn_whenAddLocation_thenItMustBeUpdated() {
        String userId = saveNewUserAndGetUserId();
        Date createdOn = new Date();
        Location location = getNewLocationWithCreatedOn(createdOn);
        Location addedLocation = service.addLocationForUser(userId, location);

        Location duplicateLocation = getDuplicateLocation(createdOn);
        Location updatedLocation = service.addLocationForUser(userId, duplicateLocation);

        assertEquals(addedLocation.getUserId(), updatedLocation.getUserId());
        assertEquals(addedLocation.getCreatedOn(), updatedLocation.getCreatedOn());
        assertNotEquals(addedLocation.getLatitude(), updatedLocation.getLatitude());
        assertNotEquals(addedLocation.getLongitude(), updatedLocation.getLongitude());
        assertEquals(1, locationRepository.count());
    }

    private void assertUserEquality(User expected, User actual) {
        assertNotNull(expected.getId());
        assertEquals(expected.getEmail(), actual.getEmail());
        assertEquals(expected.getFirstName(), actual.getFirstName());
        assertEquals(expected.getSecondName(), actual.getSecondName());
    }

    private User getNewUser() {
        return User.builder()
                .email("mirzay.mohsen@gmail.com")
                .firstName("mohsen")
                .secondName("mirzaei").
                build();
    }

    private User getUserWithNullValues() {
        return User.builder().build();
    }

    private User getNewUserWithDuplicateEmail() {
        User existingUser = service.addOrUpdate(getNewUser());
        return User.builder()
                .email(existingUser.getEmail())
                .firstName("New first name")
                .secondName("new Second name")
                .build();
    }

    private User getUpdatingValues() {
        User newUser = getNewUser();
        User updatedValues = User.builder()
                .email(newUser.getEmail())
                .firstName("Updated first Name")
                .secondName("Updated Second Name")
                .build();
        String id = service.addOrUpdate(newUser).getId();
        updatedValues.setId(id);
        return updatedValues;
    }

    private String saveNewUserAndGetUserId() {
        return service.addOrUpdate(getNewUser()).getId();
    }

    private Location getNewLocationWithCreatedOn(Date createdOn) {
        return Location.builder()
                .id(Location.LocationId.builder()
                        .createdOn(createdOn)
                        .build())
                .latitude(52.25742342295784)
                .longitude(10.540583401747602)
                .build();
    }

    private Location getDuplicateLocation(Date createdOn) {
        return Location.builder()
                .id(Location.LocationId.builder()
                        .createdOn(createdOn)
                        .build())
                .latitude(53.25742342295784)
                .longitude(11.540583401747602)
                .build();
    }
}