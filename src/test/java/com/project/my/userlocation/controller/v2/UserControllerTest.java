package com.project.my.userlocation.controller.v2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.my.userlocation.dto.in.LocationInDto;
import com.project.my.userlocation.dto.in.UserInDto;
import com.project.my.userlocation.dto.in.UserLocationInDto;
import com.project.my.userlocation.dto.out.ErrorModel;
import com.project.my.userlocation.dto.out.UserLocationOutDto;
import com.project.my.userlocation.dto.out.UserOutDto;
import com.project.my.userlocation.repository.LocationRepository;
import com.project.my.userlocation.repository.UserRepository;
import com.project.my.userlocation.utility.MessageTranslatorUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.temporal.ChronoUnit;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LocationRepository locationRepository;

    private final String users = "/v2/users";
    private final String locations = "/v2/users/locations";
    private final String lastLocation = "/v2/users/{id}/locations-last";


    @BeforeEach
    void beforeEach() {
        locationRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("PUT a new user valid info and it status is 201.")
    void givenANewUser_whenPutIt_thenItMustBeAdded() throws Exception {
        UserInDto userInDto = getNewUserInDto();

        MvcResult mvcResult = mockMvc.perform(put(users)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userInDto)))
                .andExpect(status().isCreated())
                .andReturn();

        UserOutDto userOutDto = objectMapper.readValue(mvcResult.getResponse().getContentAsByteArray(), UserOutDto.class);
        assertUserDtoEquality(userOutDto, userInDto);
        assertEquals(1, userRepository.count());
    }

    @Test
    @DisplayName("PUT a user with null values, the it responses binding error and message is asserted.")
    void givenANewUserWithNullValues_whenPutIt_thenItMustResponseError() throws Exception {
        UserInDto userInDtoWithNullValues = getUserInDtoWithNullValues();

        MvcResult mvcResult = mockMvc.perform(put(users)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userInDtoWithNullValues)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorTitle").exists())
                .andExpect(jsonPath("$.errorReasons").exists())
                .andReturn();

        ErrorModel errorModel = objectMapper.readValue(mvcResult.getResponse().getContentAsByteArray(), ErrorModel.class);
        assertEquals(errorModel.getTitle(),
                MessageTranslatorUtil.getText("exception.handler.BindException.title"));
    }

    @Test
    @DisplayName("PUT a new user with duplicate email, then it responses database error and message is asserted.")
    void givenANewUserWithDuplicateEmail_whenPutIt_thenItMustResponseError() throws Exception {
        UserInDto userInDtoWithDuplicateEmail = getNewUserInDtoWithDuplicateEmail();

        MvcResult mvcResult = mockMvc.perform(put(users)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userInDtoWithDuplicateEmail)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.errorTitle").exists())
                .andExpect(jsonPath("$.errorReasons").exists())
                .andReturn();

        ErrorModel errorModel = objectMapper.readValue(mvcResult.getResponse().getContentAsByteArray(), ErrorModel.class);
        assertEquals(errorModel.getTitle(),
                MessageTranslatorUtil.getText("exception.handler.DataBaseException.title"));
    }

    @Test
    @DisplayName("PUT request to update existing user values and it status is 200.")
    void givenAnExistUser_whenPutIt_thenItMustBeUpdated() throws Exception {
        UserInDto toUpdateInDto = getUpdateUserInDto();

        MvcResult mvcResult = mockMvc.perform(put(users)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(toUpdateInDto)))
                .andExpect(status().isOk())
                .andReturn();

        UserOutDto updatedUserOutDto = objectMapper.readValue(mvcResult.getResponse().getContentAsByteArray(), UserOutDto.class);
        assertUserDtoEquality(updatedUserOutDto, toUpdateInDto);
        assertEquals(1, userRepository.count());
    }

    @Test
    @DisplayName("PUT a user with invalid ID to update. Then response is NotFound error.")
    void givenAUserWithInvalidId_whenPutIt_thenItResponseError() throws Exception {
        UserInDto updateInvalidIdInDto = getUpdateUserInDto();
        updateInvalidIdInDto.setUserId("invalid id");

        MvcResult mvcResult = mockMvc.perform(put(users)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateInvalidIdInDto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorTitle").exists())
                .andExpect(jsonPath("$.errorReasons").exists())
                .andReturn();

        ErrorModel errorModel = objectMapper.readValue(mvcResult.getResponse().getContentAsByteArray(), ErrorModel.class);
        assertEquals(errorModel.getTitle(), MessageTranslatorUtil.getText("exception.handler.NotFoundException.title"));
        assertEquals(errorModel.getReasons().get(0), MessageTranslatorUtil.getText("service.user.get.notFound"));
    }

    @Test
    @DisplayName("POST a new Location for an exist user and response status is 201.")
    void givenAUserIdAndNewLocation_whenPostLocations_thenItMustBeAdded() throws Exception {
        String userId = saveNewUserAndGetUserOutDto().getUserId();
        Date createdOn = new Date();
        UserLocationInDto userLocationInDto = getNewLocationForUser(userId, createdOn);

        MvcResult mvcResult = mockMvc.perform(post(locations)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userLocationInDto)))
                .andExpect(status().isCreated())
                .andReturn();

        UserLocationOutDto userLocationOutDto = objectMapper.readValue(mvcResult.getResponse().getContentAsByteArray(), UserLocationOutDto.class);
        assertUserLocationsDtoEquality(userLocationOutDto, userLocationInDto);
        assertEquals(1, locationRepository.count());
    }

    @Test
    @DisplayName("POST a new location for an invalid user ID, then response is NotFound error.")
    void givenInvalidUserIdAndNewLocation_whenPostLocations_thenItResponsesError() throws Exception {
        String userId = "Invalid User ID";
        Date createdOn = new Date();
        UserLocationInDto userLocationInDto = getNewLocationForUser(userId, createdOn);

        MvcResult mvcResult = mockMvc.perform(post(locations)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userLocationInDto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorTitle").exists())
                .andExpect(jsonPath("$.errorReasons").exists())
                .andReturn();

        ErrorModel errorModel = objectMapper.readValue(mvcResult.getResponse().getContentAsByteArray(), ErrorModel.class);
        assertEquals(errorModel.getTitle(), MessageTranslatorUtil.getText("exception.handler.NotFoundException.title"));
        assertEquals(errorModel.getReasons().get(0), MessageTranslatorUtil.getText("service.user.get.notFound"));
    }

    @Test
    @DisplayName("POST a new location with null values, then it responses binding error and message is asserted.")
    void givenIANewUserLocationWithNullValues_whenPostLocations_thenItResponsesError() throws Exception {
        UserLocationInDto userLocationInDto = getLocationWithNullValues();

        MvcResult mvcResult = mockMvc.perform(post(locations)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userLocationInDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorTitle").exists())
                .andExpect(jsonPath("$.errorReasons").exists())
                .andReturn();

        ErrorModel errorModel = objectMapper.readValue(mvcResult.getResponse().getContentAsByteArray(), ErrorModel.class);
        assertEquals(errorModel.getTitle(),
                MessageTranslatorUtil.getText("exception.handler.BindException.title"));
    }

    @Test
    @DisplayName("POST a new location with createdOn date of future date, then it responses binding error and message is asserted.")
    void givenIANewUserLocationWithCreatedOnDateOfFuture_whenPostLocations_thenItResponsesError() throws Exception {
        String userId = saveNewUserAndGetUserOutDto().getUserId();
        Date createdOn = Date.from(new Date().toInstant().plus(1, ChronoUnit.DAYS));
        UserLocationInDto userLocationInDto = getNewLocationForUser(userId, createdOn);

        MvcResult mvcResult = mockMvc.perform(post(locations)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userLocationInDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorTitle").exists())
                .andExpect(jsonPath("$.errorReasons").exists())
                .andReturn();

        ErrorModel errorModel = objectMapper.readValue(mvcResult.getResponse().getContentAsByteArray(), ErrorModel.class);
        assertEquals(errorModel.getReasons().get(0),
                MessageTranslatorUtil.getText("in.userLocation.createdOn.notPastOrPresentDate"));
    }

    @Test
    @DisplayName("POST a new Location with duplicate createdOn date and it responses updated location.")
    void givenAUserIdAndNewLocationWithDuplicateCreatedOnDate_whenPostLocations_thenItMustBeUpdated() throws Exception {
        String userId = saveNewUserAndGetUserOutDto().getUserId();
        Date createdOn = new Date();
        UserLocationInDto userLocationInDto = getNewLocationForUser(userId, createdOn);
        mockMvc.perform(post(locations)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userLocationInDto)))
                .andExpect(status().isCreated())
                .andReturn();

        UserLocationInDto duplicateLocation = getNewLocationWithDuplicateCreatedOn(userId, createdOn);

        MvcResult mvcResult = mockMvc.perform(post(locations)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(duplicateLocation)))
                .andExpect(status().isCreated())
                .andReturn();

        UserLocationOutDto userLocationOutDto = objectMapper.readValue(mvcResult.getResponse().getContentAsByteArray(), UserLocationOutDto.class);
        assertUserLocationsDtoEquality(userLocationOutDto, duplicateLocation);
        assertEquals(1, locationRepository.count());
    }

    @Test
    @DisplayName("GET last location of a user when exist then response is OK.")
    void givenAUserIdAndThreeLocations_whenGetLastLocation_thenItMustBeReturned() throws Exception {
        UserOutDto userOutDto = saveNewUserAndGetUserOutDto();
        String userId = userOutDto.getUserId();
        Date lastDate = new Date();
        Date oneDayBefore = Date.from(lastDate.toInstant().minus(1, ChronoUnit.DAYS));
        Date twoDaysBefore = Date.from(lastDate.toInstant().minus(2, ChronoUnit.DAYS));

        UserLocationInDto lastUserLocationIndDto = getNewLocationForUser(userId, lastDate);
        mockMvc.perform(post(locations)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(lastUserLocationIndDto)))
                .andExpect(status().isCreated())
                .andReturn();

        UserLocationInDto userLocationInDto2 = getNewLocationForUser(userId, oneDayBefore);
        mockMvc.perform(post(locations)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userLocationInDto2)))
                .andExpect(status().isCreated())
                .andReturn();

        UserLocationInDto userLocationInDto3 = getNewLocationForUser(userId, twoDaysBefore);
        mockMvc.perform(post(locations)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userLocationInDto3)))
                .andExpect(status().isCreated())
                .andReturn();

        assertEquals(3, locationRepository.count());

        MvcResult mvcResult = mockMvc.perform(get(lastLocation, userId))
                .andExpect(status().isOk())
                .andReturn();

        UserLocationOutDto userLocationOutDto = objectMapper.readValue(mvcResult.getResponse().getContentAsByteArray(), UserLocationOutDto.class);
        assertEquals(userLocationOutDto.getUserId(), userOutDto.getUserId());
        assertNotNull(userLocationOutDto.getEmail());
        assertEquals(userLocationOutDto.getEmail(), userOutDto.getEmail());
        assertNotNull(userLocationOutDto.getFirstName());
        assertEquals(userLocationOutDto.getFirstName(), userOutDto.getFirstName());
        assertNotNull(userLocationOutDto.getSecondName());
        assertEquals(userLocationOutDto.getSecondName(), userOutDto.getSecondName());
        assertNull(userLocationOutDto.getLocation().getCreatedOn());
        assertEquals(userLocationOutDto.getLocation().getLatitude(), lastUserLocationIndDto.getLocation().getLatitude());
        assertEquals(userLocationOutDto.getLocation().getLongitude(), lastUserLocationIndDto.getLocation().getLongitude());
    }

    @Test
    @DisplayName("GET last location of a user when there is no location then response is NotFound.")
    void givenAUserIdWithoutAnyLocations_whenGetLastLocation_thenItMustBeNoContent() throws Exception {
        UserOutDto userOutDto = saveNewUserAndGetUserOutDto();
        String userId = userOutDto.getUserId();

        assertEquals(0, locationRepository.count());

        MvcResult mvcResult = mockMvc.perform(get(lastLocation, userId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorTitle").exists())
                .andExpect(jsonPath("$.errorReasons").exists())
                .andReturn();

        ErrorModel errorModel = objectMapper.readValue(mvcResult.getResponse().getContentAsByteArray(), ErrorModel.class);
        assertEquals(errorModel.getReasons().get(0),
                MessageTranslatorUtil.getText("service.user.lastLocation.get.notFound"));
    }

    private void assertUserDtoEquality(UserOutDto expected, UserInDto actual) {
        assertNotNull(expected.getUserId());
        assertEquals(expected.getEmail(), actual.getEmail());
        assertEquals(expected.getFirstName(), actual.getFirstName());
        assertEquals(expected.getSecondName(), actual.getSecondName());
    }

    private UserInDto getNewUserInDto() {
        return UserInDto.builder()
                .email("mirzay.mohsen@gmail.com")
                .firstName("mohsen")
                .secondName("mirzaei")
                .build();
    }

    private UserInDto getUserInDtoWithNullValues() {
        return UserInDto.builder().build();
    }

    private UserInDto getNewUserInDtoWithDuplicateEmail() throws Exception {
        UserOutDto userOutDto = saveNewUserAndGetUserOutDto();
        return UserInDto.builder()
                .email(userOutDto.getEmail())
                .firstName("different name")
                .secondName("different name")
                .build();
    }

    private UserInDto getUpdateUserInDto() throws Exception {
        UserOutDto userOutDto = saveNewUserAndGetUserOutDto();
        return UserInDto.builder()
                .userId(userOutDto.getUserId())
                .email(userOutDto.getEmail())
                .firstName("updated name")
                .secondName("updated name")
                .build();
    }

    private UserOutDto saveNewUserAndGetUserOutDto() throws Exception {
        UserInDto userInDto = getNewUserInDto();

        MvcResult mvcResult = mockMvc.perform(put(users)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userInDto)))
                .andExpect(status().isCreated())
                .andReturn();

        return objectMapper.readValue(mvcResult.getResponse().getContentAsByteArray(), UserOutDto.class);
    }

    private UserLocationInDto getNewLocationForUser(String userId, Date createdOn) {
        return UserLocationInDto.builder()
                .userId(userId)
                .createdOn(createdOn)
                .location(LocationInDto.builder()
                        .latitude(52.25742342295784)
                        .longitude(10.540583401747602)
                        .build())
                .build();
    }

    private void assertUserLocationsDtoEquality(UserLocationOutDto userLocationOutDto, UserLocationInDto userLocationInDto) {
        assertNotNull(userLocationOutDto.getUserId());
        assertNotNull(userLocationOutDto.getLocation());
        assertEquals(userLocationOutDto.getLocation().getCreatedOn(), userLocationInDto.getCreatedOn());
        assertEquals(userLocationOutDto.getLocation().getLatitude(), userLocationInDto.getLocation().getLatitude());
        assertEquals(userLocationOutDto.getLocation().getLongitude(), userLocationInDto.getLocation().getLongitude());
    }

    private UserLocationInDto getLocationWithNullValues() {
        return UserLocationInDto.builder().build();
    }

    private UserLocationInDto getNewLocationWithDuplicateCreatedOn(String userId, Date createdOn) {
        return UserLocationInDto.builder()
                .userId(userId)
                .createdOn(createdOn)
                .location(LocationInDto.builder()
                        .latitude(53.25742342295784)
                        .longitude(11.540583401747602)
                        .build())
                .build();

    }
}