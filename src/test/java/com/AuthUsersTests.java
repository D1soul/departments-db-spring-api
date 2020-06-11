package com;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import ru.d1soul.departments.SpringRestApiRunner;
import ru.d1soul.departments.api.service.authentification.RoleService;
import ru.d1soul.departments.api.service.authentification.UserService;
import ru.d1soul.departments.model.Role;
import ru.d1soul.departments.model.User;
import ru.d1soul.departments.security.jwt.JwtTokenProvider;
import ru.d1soul.departments.security.jwt.dto.AuthUser;
import ru.d1soul.departments.security.jwt.dto.JwtUserDto;
import ru.d1soul.departments.security.jwt.dto.PasswordChangingUser;

import java.util.*;
import java.util.stream.Collectors;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = SpringRestApiRunner.class)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application.yaml")
public class AuthUsersTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private ObjectMapper objectMapper;

    @Qualifier("userDetailsServiceImpl")
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    RoleService roleService;

    @Autowired
    UserService userService;

    private static final String URL_REGISTRATION = "http://localhost:8080/departments-app/auth/registration";
    private static final String URL_LOG_IN = "http://localhost:8080/departments-app/auth/login";
    private static final String URL_CHANGE_PASSWORD = "http://localhost:8080/departments-app/auth/changing-password";
    private static final String URL_ALL_USERS = "http://localhost:8080/departments-app/auth/users";
    private static final String URL_USER_DETAIL = "http://localhost:8080/departments-app/auth/users/{username}";
    private static final String URL_UPDATE_USER = "http://localhost:8080/departments-app/auth/users/{username}";
    private static final String URL_DELETE_USER = "http://localhost:8080/departments-app/auth/users/{username}";

    @Test
    public void registrationTest() throws Exception {
        Set<Role> roles = roleService.findAllRoles();
        mockMvc.perform(MockMvcRequestBuilders
            .post(URL_REGISTRATION)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(new User(
               null, "TestUser", "testuser@testmail.com", "TestPassword", "TestPassword",
               new Date(new GregorianCalendar(1970, Calendar.SEPTEMBER, 16).getTime().getTime()),
               "male", false, roles))))
            .andExpect(status().isCreated())
            .andDo(print());
    }


    public String getToken(){
        UserDetails userDetails = userDetailsService.loadUserByUsername("admin");
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                "admin", "admin№1"));
        String username = userDetails.getUsername();
        String password = userDetails.getPassword();
        Set<String> roles = userDetails.getAuthorities().stream().map(
                GrantedAuthority::getAuthority).collect(Collectors.toSet());
        return  jwtTokenProvider.createToken(new JwtUserDto(username, password, roles));
    }


    @Test
    public void logInTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .post(URL_LOG_IN)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new AuthUser(
                        "TestUser", "TestPassword"))))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void changePasswordTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .put(URL_CHANGE_PASSWORD)
                .header("Authorization", "Bearer " + getToken())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new PasswordChangingUser(
                        "TestUser",
                        "TestPassword",
                        "TestPassword",
                        "TestPassword"))))
                .andExpect(status().isOk())
                .andDo(print());
    }


    @Test
    public void findAllUsersTest() throws Exception
    {        mockMvc.perform(MockMvcRequestBuilders
            .get(URL_ALL_USERS)
            .header("Authorization", "Bearer " + getToken())
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(print());
    }

    @Test
    public void findUserByUsernameTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .get(URL_USER_DETAIL, "TestUser" )
                .header("Authorization", "Bearer " + getToken())
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());
    }

    @Test
    public  void updateMainDeptEmplTest() throws Exception{
        Set<Role> roles = roleService.findAllRoles();
        mockMvc.perform(MockMvcRequestBuilders
                .put(URL_UPDATE_USER, "TestUser")
                .header("Authorization", "Bearer " + getToken())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new User(
                        null, "TestUser", "testuser@testmail.com", "TestPassword", "TestPassword",
                        new Date(new GregorianCalendar(1992, Calendar.MARCH, 21).getTime().getTime()),
                        "female", false, roles)))
        )
                .andExpect(status().isOk())
                .andDo(print());
    }


    @Test
    public void deleteUserTest() throws Exception
    {
        mockMvc.perform( MockMvcRequestBuilders
                .delete(URL_DELETE_USER, "TestUser")
                .header("Authorization", "Bearer " + getToken()))
                .andExpect(status().isNoContent())
                .andDo(print());
    }
}
