package com.project.my.userlocation.controller.v2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.my.userlocation.dto.ErrorModel;
import com.project.my.userlocation.dto.UserInDto;
import com.project.my.userlocation.dto.UserOutDto;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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

    private final String users = "/v2/users";


    @BeforeEach
    void beforeEach() {
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
}