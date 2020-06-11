package com;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import ru.d1soul.departments.SpringRestApiRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.d1soul.departments.model.MainDepartment;
import ru.d1soul.departments.security.jwt.JwtTokenProvider;
import ru.d1soul.departments.security.jwt.dto.JwtUserDto;
import java.util.Set;
import java.util.stream.Collectors;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = SpringRestApiRunner.class)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application.yaml")
public class MainDepartmentsTests {

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

    private static final String URL_FIND_ALL_MAIN_DEPARTMENTS = "http://localhost:8080/departments-app/main_departments";
    private static final String URL_FIND_MAIN_DEP_BY_NAME = "http://localhost:8080/departments-app/main_departments/{name}";
    private static final String URL_UPDATE_MAIN_DEP = "http://localhost:8080/departments-app/main_departments/{name}";
    private static final String URL_CREATE_MAIN_DEP = "http://localhost:8080/departments-app/main_departments/";
    private static final String URL_DELETE_MAIN_DEP = "http://localhost:8080/departments-app/main_departments/{name}";


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
    public void getAllMainDepTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .get(URL_FIND_ALL_MAIN_DEPARTMENTS)
                .header("Authorization", "Bearer " + getToken())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void findMaiDepByName() throws Exception{

        mockMvc.perform(MockMvcRequestBuilders
                .get(URL_FIND_MAIN_DEP_BY_NAME, "Департамент архитектуры")
                .header("Authorization", "Bearer " + getToken())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public  void createMainDepTest() throws Exception{

        mockMvc.perform(MockMvcRequestBuilders
                .post(URL_CREATE_MAIN_DEP)
                .header("Authorization", "Bearer " + getToken())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new MainDepartment(
                        "Департамент сельского хозяйства"))))
                .andExpect(status().isCreated())
                .andDo(print());
    }

    @Test
    public  void updateMainDepTest() throws Exception{

        mockMvc.perform(MockMvcRequestBuilders
                .put(URL_UPDATE_MAIN_DEP, "Департамент сельского хозяйства")
                .header("Authorization", "Bearer " + getToken())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new MainDepartment(
                        "Департамент сельского хозяйства и агрономии"))))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void deleteMainDepTest() throws Exception
    {
        mockMvc.perform( MockMvcRequestBuilders
                .delete(URL_DELETE_MAIN_DEP, "Департамент сельского хозяйства и агрономии")
                .header("Authorization", "Bearer " + getToken()))
                .andExpect(status().isNoContent());

        mockMvc.perform(MockMvcRequestBuilders
                .get(URL_FIND_ALL_MAIN_DEPARTMENTS)
                .header("Authorization", "Bearer " + getToken())
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

}
