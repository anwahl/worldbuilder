package com.wahlhalla.worldbuilder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.LinkedHashSet;
import java.util.Set;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import com.wahlhalla.worldbuilder.role.ERole;
import com.wahlhalla.worldbuilder.role.Role;
import com.wahlhalla.worldbuilder.role.RoleRepository;
import com.wahlhalla.worldbuilder.security.AuthController;
import com.wahlhalla.worldbuilder.security.payload.request.EmailChangeRequest;
import com.wahlhalla.worldbuilder.security.payload.request.LoginRequest;
import com.wahlhalla.worldbuilder.security.payload.request.PasswordChangeRequest;
import com.wahlhalla.worldbuilder.security.payload.request.SignupRequest;
import com.wahlhalla.worldbuilder.user.User;
import com.wahlhalla.worldbuilder.user.UserRepository;
import com.wahlhalla.worldbuilder.util.Util;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(Lifecycle.PER_CLASS)
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
public class AuthControllerTests {
    
    
    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    UserRepository userRepository;

    
    @Autowired
    RoleRepository roleRepository;

    @Autowired
    AuthController authController;

    @Autowired
    private MockMvc mvc;
    
    @BeforeAll
    void create() throws Exception {
        roleRepository.save(new Role(ERole.ROLE_USER));
        roleRepository.save(new Role(ERole.ROLE_ADMIN));
        roleRepository.save(new Role(ERole.ROLE_MODERATOR));
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("user@email.com");  
        signupRequest.setPassword("password");
        Set<String> roles = new LinkedHashSet<>();
        roles.add("ROLE_USER");
        signupRequest.setRole(roles);
        signupRequest.setUsername("user");
        
        mvc.perform(post("/api/auth/signup")
            .content(Util.asJsonString(signupRequest))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON));
    }


    @Test
    public void contextLoads() throws Exception {
            assertThat(authController).isNotNull();
    }

    @Test
    public void givenSignup_thenStatus200()
        throws Exception {
        
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("email@email.com");  
        signupRequest.setPassword("password");
        Set<String> roles = new LinkedHashSet<>();
        roles.add("ROLE_USER");
        signupRequest.setRole(roles);
        signupRequest.setUsername("username");
        
        mvc.perform(post("/api/auth/signup")
            .content(Util.asJsonString(signupRequest))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().string((containsString("User registered successfully!"))));
            assertThat(userRepository.count()).isEqualTo(2);
    }

    @Test
    public void givenSignup_whenGivenSameUsername_thenStatus400()
        throws Exception {
        
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("email@email.com");  
        signupRequest.setPassword("password");
        Set<String> roles = new LinkedHashSet<>();
        roles.add("ROLE_USER");
        signupRequest.setRole(roles);
        signupRequest.setUsername("username");

        mvc.perform(post("/api/auth/signup")
            .content(Util.asJsonString(signupRequest))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON));

        SignupRequest signupRequest2 = new SignupRequest();
        signupRequest2.setEmail("email2@email2.com");  
        signupRequest2.setPassword("password");
        signupRequest2.setRole(roles);
        signupRequest2.setUsername("uSerNaMe");
        
        mvc.perform(post("/api/auth/signup")
            .content(Util.asJsonString(signupRequest2))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().is4xxClientError())
            .andExpect(content().string((containsString("Username is already taken!"))));
    }

    @Test
    @WithMockCustomUser
    public void givenUserChangeEmail_thenStatus200()
        throws Exception {
        EmailChangeRequest emailChangeRequest = new EmailChangeRequest();
        emailChangeRequest.setNewEmail("newEmail@newEmail.com");
        emailChangeRequest.setUsername("user");
        User user = userRepository.findByUsernameIgnoreCase("user").get();

        mvc.perform(put("/api/auth/changeEmail/" + user.getId())
            .content(Util.asJsonString(emailChangeRequest))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().string((containsString("Email changed successfully!"))));
        User user2 = userRepository.findByUsernameIgnoreCase("user").get();
            assert(user2.getEmail().equals("newEmail@newEmail.com"));
    }

    @Test
    @WithMockCustomUser
    public void givenUserChangePassword_thenStatus200()
        throws Exception {
        PasswordChangeRequest passwordChangeRequest = new PasswordChangeRequest();
        passwordChangeRequest.setNewPassword("newPassword");
        passwordChangeRequest.setOldPassword("password");
        User user = userRepository.findByUsernameIgnoreCase("user").get();

        mvc.perform(put("/api/auth/changePassword/" + user.getId())
            .content(Util.asJsonString(passwordChangeRequest))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().string((containsString("Password changed successfully!"))));
    }

    @Test
    @WithMockCustomUser
    public void givenUserChangePassword_givenWrongPassword_thenStatus4xx()
        throws Exception {
        PasswordChangeRequest passwordChangeRequest = new PasswordChangeRequest();
        passwordChangeRequest.setNewPassword("newPassword");
        passwordChangeRequest.setOldPassword("wrong");
        User user = userRepository.findByUsernameIgnoreCase("user").get();

        mvc.perform(put("/api/auth/changePassword/" + user.getId())
            .content(Util.asJsonString(passwordChangeRequest))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().is4xxClientError())
            .andExpect(content().string((containsString("Password is incorrect!"))));
    }

    @Test
    public void givenUser_signIn_thenStatus200()
        throws Exception {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("email@email.com");  
        signupRequest.setPassword("password");
        Set<String> roles = new LinkedHashSet<>();
        roles.add("ROLE_USER");
        signupRequest.setRole(roles);
        signupRequest.setUsername("username");

        mvc.perform(post("/api/auth/signup")
            .content(Util.asJsonString(signupRequest))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON));
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setPassword("password");
        loginRequest.setUsername("username");

        mvc.perform(post("/api/auth/signin")
            .content(Util.asJsonString(loginRequest))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("username", is("username")));
    }

    @Test
    public void givenUser_signInCaseInsensitive_thenStatus200()
        throws Exception {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("email@email.com");  
        signupRequest.setPassword("password");
        Set<String> roles = new LinkedHashSet<>();
        roles.add("ROLE_USER");
        signupRequest.setRole(roles);
        signupRequest.setUsername("username");

        mvc.perform(post("/api/auth/signup")
            .content(Util.asJsonString(signupRequest))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON));
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setPassword("password");
        loginRequest.setUsername("USeRnAmE");

        mvc.perform(post("/api/auth/signin")
            .content(Util.asJsonString(loginRequest))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("username", is("username")));
    }

    @Test
    public void givenUser_signIn_whenWrongPassword_thenStatus4xx()
        throws Exception {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("email@email.com");  
        signupRequest.setPassword("password");
        Set<String> roles = new LinkedHashSet<>();
        roles.add("ROLE_USER");
        signupRequest.setRole(roles);
        signupRequest.setUsername("username");

        mvc.perform(post("/api/auth/signup")
            .content(Util.asJsonString(signupRequest))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON));
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setPassword("wrong");
        loginRequest.setUsername("username");

        mvc.perform(post("/api/auth/signin")
            .content(Util.asJsonString(loginRequest))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockCustomUser
    public void givenUser_whenDeleteUser_thenStatus200()
        throws Exception {
        User user = userRepository.findByUsernameIgnoreCase("user").get();

        mvc.perform(delete("/api/auth/deleteUser/" + user.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().string((containsString("You've been signed out!"))));
    }
}
