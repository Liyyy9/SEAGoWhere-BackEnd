package com.example.seagowhere.Controller;

import com.example.seagowhere.Model.EnumRole;
import com.example.seagowhere.Model.Users;
import com.example.seagowhere.Repository.BookingRepository;
import com.example.seagowhere.Repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;

    @Autowired
    BookingRepository bookingRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String API_ENDPOINT = "/api/users";

    private Users user1, user2;
    private List<Users> usersList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        bookingRepository.deleteAll();

        user1 = new Users();
        user1.setFirstName("User");
        user1.setLastName("1");
        user1.setEmail("user1@gmail.com");
        user1.setPassword("password");
        user1.setNumber("98653298");
        user1.setRole(EnumRole.USER);

        user2 = new Users();
        user2.setFirstName("User");
        user2.setLastName("2");
        user2.setEmail("user2@gmail.com");
        user2.setPassword("password");
        user2.setNumber("98326532");
        user2.setRole(EnumRole.ADMIN);

        usersList.add(user1);
        usersList.add(user2);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getAllUsers() throws Exception{
        userRepository.saveAll(usersList);

        ResultActions resultActions = mockMvc.perform(get(API_ENDPOINT));

        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()").value(usersList.size()));
    }

    @Test
    void getUserById() throws Exception{
        userRepository.save(user1);

        ResultActions resultActions = mockMvc.perform(get(API_ENDPOINT.concat("/{id}"),user1.getId()));

        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.firstName").value(user1.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(user1.getLastName()))
                .andExpect(jsonPath("$.email").value(user1.getEmail()))
                .andExpect(jsonPath("$.number").value(user1.getNumber()))
                .andExpect(jsonPath("$.password").value(user1.getPassword()))
                .andExpect(jsonPath("$.role").value(user1.getRole().name()));
    }

    @Test
    void createUser() throws Exception {
        Users userNew = new Users();
        userNew.setFirstName("User");
        userNew.setLastName("New");
        userNew.setEmail("usernew@gmail.com");
        userNew.setPassword("password");
        userNew.setNumber("99999999");
        userNew.setRole(EnumRole.USER);

        String requestBody = objectMapper.writeValueAsString(userNew);

        ResultActions resultActions = mockMvc.perform(post(API_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
        );

        resultActions.andExpect(status().isCreated())
                .andDo(print())
                .andExpect(jsonPath("$.firstName").value(userNew.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(userNew.getLastName()))
                .andExpect(jsonPath("$.email").value(userNew.getEmail()))
                .andExpect(jsonPath("$.number").value(userNew.getNumber()))
                .andExpect(jsonPath("$.role").value(userNew.getRole().name()))
                .andExpect(jsonPath("$.authorities").doesNotExist());
    }

    @Test
    void updateUser() throws Exception {
        userRepository.save(user1);

        Users updatedUser = new Users();
        updatedUser.setFirstName("UpdatedUser");
        updatedUser.setLastName("1");
        updatedUser.setEmail("updateduser1@gmail.com");
        updatedUser.setPassword("newpassword");
        updatedUser.setNumber("12345678");
        updatedUser.setRole(EnumRole.USER);

        String requestBody = objectMapper.writeValueAsString(updatedUser);

        ResultActions resultActions = mockMvc.perform(put(API_ENDPOINT.concat("/{id}"), user1.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
        );

        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.firstName").value(updatedUser.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(updatedUser.getLastName()))
                .andExpect(jsonPath("$.email").value(updatedUser.getEmail()))
                .andExpect(jsonPath("$.number").value(updatedUser.getNumber()))
                .andExpect(jsonPath("$.role").value(updatedUser.getRole().name()));
    }

    @Test
    void deleteUser() throws Exception {
        userRepository.save(user1);
        Users deleteUser1 = userRepository.findById(user1.getId()).get();
        String expectedResponse = String.format("Users %s deleted successfully", deleteUser1.getUsername());

        ResultActions resultActions = mockMvc.perform(delete(API_ENDPOINT.concat("/{id}"), user1.getId())
                .contentType(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andExpect(result -> assertEquals(expectedResponse, result.getResponse().getContentAsString()));
    }

    @Test
    void getUserByEmail() throws Exception {
        userRepository.save(user1);

        ResultActions resultActions = mockMvc.perform(get(API_ENDPOINT.concat("/email"))
                .param("email", user1.getEmail()));

        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.firstName").value(user1.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(user1.getLastName()))
                .andExpect(jsonPath("$.email").value(user1.getEmail()))
                .andExpect(jsonPath("$.number").value(user1.getNumber()))
                .andExpect(jsonPath("$.role").value(user1.getRole().name()));
    }
}