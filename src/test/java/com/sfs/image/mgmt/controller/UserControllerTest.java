package com.sfs.image.mgmt.controller;

import com.sfs.image.mgmt.dto.UserProfile;
import com.sfs.image.mgmt.entity.User;
import com.sfs.image.mgmt.exception.AuthenticationException;
import com.sfs.image.mgmt.exception.UserNotFoundException;
import com.sfs.image.mgmt.service.IUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    private IUserService userService;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    public void testRegisterUser() throws Exception {
        User user = new User();
        user.setSfsUser("testuser");
        user.setSfsUserpassword("password");

        when(userService.registerUser(any(User.class))).thenReturn(user);

        mockMvc.perform(post("/sfs/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"sfsUser\": \"testuser\", \"sfsUserpassword\": \"password\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sfsUser").value("testuser"));

        verify(userService, times(1)).registerUser(any(User.class));
    }

    @Test
    public void testLoginUser_Success() throws Exception {
        when(userService.authenticateUser(anyString(), anyString())).thenReturn("login success");

        mockMvc.perform(post("/sfs/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"sfsUser\": \"testuser\", \"sfsUserpassword\": \"password\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("login success"));

        verify(userService, times(1)).authenticateUser(eq("testuser"), eq("password"));
    }

    @Test
    public void testLoginUser_Failure() throws Exception {
        when(userService.authenticateUser(anyString(), anyString())).thenThrow(new AuthenticationException("Invalid username or password"));

        mockMvc.perform(post("/sfs/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"sfsUser\": \"testuser\", \"sfsUserpassword\": \"wrongpassword\"}"))
                .andExpect(content().string("login failure"));

        verify(userService, times(1)).authenticateUser(eq("testuser"), eq("wrongpassword"));
    }

    @Test
    public void testGetUserProfile_Success() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setSfsUser("testuser");

        when(userService.getUser(anyString())).thenReturn(Optional.of(user));

        mockMvc.perform(get("/sfs/users/testuser"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.username").value("testuser"));

        verify(userService, times(1)).getUser(eq("testuser"));
    }

    @Test
    public void testGetUserProfile_Failure() throws Exception {
        when(userService.getUser(anyString())).thenReturn(Optional.empty());

        mockMvc.perform(get("/sfs/users/unknownuser"))
                .andExpect(status().isNotFound())
                .andReturn();

        verify(userService, times(1)).getUser(eq("unknownuser"));
    }
}
