package com.faceit.usermanagementtool.controller;

import com.faceit.usermanagementtool.openapi.model.UserRequest;
import com.faceit.usermanagementtool.service.impl.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Mock
    private UserServiceImpl userService;

    @InjectMocks
    private UserController userController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    private UserRequest firstUserRequestCreation(){
        UserRequest userRequest = new UserRequest();
        userRequest.setId("1992");
        userRequest.setFirstName("firstName");
        userRequest.setLastName("lastName");
        userRequest.setNickname("nickname");
        userRequest.setCountry("UK");
        userRequest.setEmail("email");
        userRequest.setPassword("pass");
        return userRequest;
    }
    private UserRequest userRequestCreation(){
        UserRequest userRequest = new UserRequest();
        userRequest.setId(String.valueOf((int) (Math.random() * 1000)));
        userRequest.setFirstName("firstName");
        userRequest.setLastName("lastName");
        userRequest.setNickname("nickname");
        userRequest.setCountry("CU");
        userRequest.setEmail("email");
        userRequest.setPassword("pass");
        return userRequest;
    }

    private UserRequest userUpdateRequest(){
        UserRequest userRequest = new UserRequest();
        userRequest.setId("1992");
        userRequest.setFirstName("firstName");
        userRequest.setLastName("lastName");
        userRequest.setNickname("nuevo");
        userRequest.setCountry("CU");
        userRequest.setEmail("nuevo");
        userRequest.setPassword("pass");
        return userRequest;
    }

    @Test
    public void restCreateReturns201() throws Exception {
        this.mockMvc.perform(
            MockMvcRequestBuilders.post("/users")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(this.firstUserRequestCreation())))
            .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    public void restCreateReturns500() throws Exception {
        this.mockMvc.perform(
                    MockMvcRequestBuilders.post("/users")
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(MockMvcResultMatchers.status().isInternalServerError());
    }

    @Test
    public void restReadReturns200() throws Exception {
        this.mockMvc.perform(
                MockMvcRequestBuilders.get("/users")
                        .queryParam("country", "CU")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void restReadReturns404() throws Exception {
        this.mockMvc.perform(
                        MockMvcRequestBuilders.get("/users")
                                .queryParam("id", "nope")
                                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    //@Test
    public void restUpdateReturns200() throws Exception {
        this.mockMvc.perform(
                MockMvcRequestBuilders.patch("/users/{id}", "1992")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(this.userUpdateRequest()))
                        .accept(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void restUpdateReturns404() throws Exception {
        this.mockMvc.perform(
        MockMvcRequestBuilders.patch("/users/{id}", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(this.userUpdateRequest()))
                .accept(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
    
    @Test
    public void restDeleteReturns404() throws Exception {
        this.mockMvc.perform(
                        MockMvcRequestBuilders.delete("/users/{id}", "nope")
                                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void restDeleteReturns200() throws Exception {
        this.mockMvc.perform(
                    MockMvcRequestBuilders.delete("/users/{id}", "1992")
                            .accept(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
