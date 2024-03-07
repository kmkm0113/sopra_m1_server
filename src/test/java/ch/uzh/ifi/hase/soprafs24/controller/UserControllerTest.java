package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserPostDTO;
import ch.uzh.ifi.hase.soprafs24.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * UserControllerTest
 * This is a WebMvcTest which allows to test the UserController i.e. GET/POST
 * request without actually sending them over the network.
 * This tests if the UserController works.
 */
@WebMvcTest(UserController.class)
public class UserControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private UserService userService;

  @Test
  public void createUser_validInput_userCreated() throws Exception {
    // given
    User user = new User();
    user.setId(1L);
    user.setUsername("testUsername");
    user.setStatus(UserStatus.ONLINE);

    UserPostDTO userPostDTO = new UserPostDTO();
    userPostDTO.setUsername("testUsername");
    userPostDTO.setPassword("password");

    given(userService.createUser(Mockito.any())).willReturn(user);

    // when/then -> do the request + validate the result
    MockHttpServletRequestBuilder postRequest = post("/users")
      .contentType(MediaType.APPLICATION_JSON)
      .content(asJsonString(userPostDTO));

    // then
    mockMvc.perform(postRequest)
      .andExpect(status().isCreated())
      .andExpect(jsonPath("$.id", is(user.getId().intValue())))
      .andExpect(jsonPath("$.username", is(user.getUsername())))
      .andExpect(jsonPath("$.status", is(user.getStatus().toString())));
  }

  @Test
  public void invalidUsernameInput_whenPostUser_thenReturnConflict() throws Exception {
    UserPostDTO userPostDTO = new UserPostDTO();
    userPostDTO.setUsername("testUsername");

    given(userService.createUser(Mockito.any())).willThrow(new ResponseStatusException(HttpStatus.CONFLICT, "username exists already"));

    // when/then -> do the request + validate the result
    MockHttpServletRequestBuilder postRequest = post("/users")
      .contentType(MediaType.APPLICATION_JSON)
      .content(asJsonString(userPostDTO));

    // then
    mockMvc.perform(postRequest)
      .andExpect(status().isConflict());
  }

  @Test
  public void givenUsers_whenGetUsers_thenReturnJsonArray() throws Exception {
    // given
    User user = new User();
    user.setUsername("firstname@lastname");
    user.setStatus(UserStatus.OFFLINE);

    List<User> allUsers = Collections.singletonList(user);

    // this mocks the UserService -> we define above what the userService should
    // return when getUsers() is called
    given(userService.getUsers()).willReturn(allUsers);

    // when
    MockHttpServletRequestBuilder getRequest = get("/users")
      .contentType(MediaType.APPLICATION_JSON);

    // then
    mockMvc.perform(getRequest).andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$[0].username", is(user.getUsername())))
        .andExpect(jsonPath("$[0].status", is(user.getStatus().toString())));
  }

  @Test
  public void validId_whenGetUser_thenReturnUser() throws Exception {
    // given
    User user = new User();
    user.setId(1L);
    user.setUsername("username");
    user.setStatus(UserStatus.ONLINE);
    String dateString = "2024-03-07T18:24:23.398+00:00";
    Date date = Date.from(Instant.parse(dateString));
    user.setCreationDate(date);
    user.setBirthday(date);

    given(userService.getUserProfile(user.getId())).willReturn(user);

    // when/then -> do the request + validate the result
    MockHttpServletRequestBuilder getRequest = get("/users/{userId}", user.getId())
      .contentType(MediaType.APPLICATION_JSON);

    // then
    mockMvc.perform(getRequest)
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.id", is(user.getId().intValue())))
      .andExpect(jsonPath("$.username", is(user.getUsername())))
      .andExpect(jsonPath("$.status", is(user.getStatus().toString())))
      .andExpect(jsonPath("$.creationDate", is(dateString)))
      .andExpect(jsonPath("$.birthday", is(dateString)));
  }

  @Test
  public void invalidId_whenGetUser_thenNotFound() throws Exception {
    // given
    User user = new User();
    user.setId(1L);

    given(userService.getUserProfile(user.getId())).willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

    // when/then -> do the request + validate the result
    MockHttpServletRequestBuilder getRequest = get("/users/{userId}", user.getId())
      .contentType(MediaType.APPLICATION_JSON);

    // then
    mockMvc.perform(getRequest)
      .andExpect(status().isNotFound());
  }

  @Test
  public void validInput_whenPutUser_thenReturnNoContent() throws Exception {
    // given
    User user = new User();
    user.setId(1L);

    doNothing().when(userService).updateUser(Mockito.any());

    // when/then -> do the request + validate the result
    MockHttpServletRequestBuilder putRequest = put("/users/{userId}", user.getId())
      .contentType(MediaType.APPLICATION_JSON)
      .content(asJsonString(user));

    // then
    mockMvc.perform(putRequest)
      .andExpect(status().isNoContent());
  }

  @Test
  public void invalidId_whenPutUser_thenReturnNotFound() throws Exception {
    // given
    User user = new User();
    user.setId(1L);

    doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND))
      .when(userService)
      .updateUser(Mockito.any());

    // when/then -> do the request + validate the result
    MockHttpServletRequestBuilder putRequest = put("/users/{userId}", user.getId())
      .contentType(MediaType.APPLICATION_JSON)
      .content(asJsonString(user));

    // then
    mockMvc.perform(putRequest)
      .andExpect(status().isNotFound());
  }


  /**
   * Helper Method to convert userPostDTO into a JSON string such that the input
   * can be processed
   * Input will look like this: {"name": "Test User", "username": "testUsername"}
   * 
   * @param object
   * @return string
   */
  private String asJsonString(final Object object) {
    try {
      return new ObjectMapper().writeValueAsString(object);
    } catch (JsonProcessingException e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
          String.format("The request body could not be created.%s", e.toString()));
    }
  }
}