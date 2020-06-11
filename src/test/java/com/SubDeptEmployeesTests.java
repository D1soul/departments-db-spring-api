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
import ru.d1soul.departments.api.service.department.SubDepartmentService;
import ru.d1soul.departments.model.SubDeptEmployee;
import ru.d1soul.departments.security.jwt.JwtTokenProvider;
import ru.d1soul.departments.security.jwt.dto.JwtUserDto;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Set;
import java.util.stream.Collectors;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = SpringRestApiRunner.class)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application.yaml")
public class SubDeptEmployeesTests {

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
    private SubDepartmentService subDepartmentService;

    private static final String URL_FIND_ALL_SUB_DEPT_EMPL = "http://localhost:8080/departments-app/sub-dept_employees";
    private static final String URL_FIND_SUB_DEPT_EMPL_BY_FULL_NAME =
            "http://localhost:8080/departments-app/sub-dept_employees/{lastName}/{firstName}/{middleName}";
    private static final String URL_UPDATE_SUB_DEPT_EMPL =
            "http://localhost:8080/departments-app/sub-dept_employees/{lastName}/{firstName}/{middleName}";
    private static final String URL_CREATE_SUB_DEPT_EMPL = "http://localhost:8080/departments-app/sub-dept_employees";
    private static final String URL_DELETE_SUB_DEPT_EMPL =
            "http://localhost:8080/departments-app/sub-dept_employees/{lastName}/{firstName}/{middleName}";

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
    public void findAllSubDeptEmplsTest() throws Exception
    {        mockMvc.perform(MockMvcRequestBuilders
                .get(URL_FIND_ALL_SUB_DEPT_EMPL)
                .header("Authorization", "Bearer " + getToken())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void findSubDeptEmplByIdTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .get(URL_FIND_SUB_DEPT_EMPL_BY_FULL_NAME, "Кулибин", "Алексей", "Тимурович")
                .header("Authorization", "Bearer " + getToken())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public  void createSubDeptEmplTest() throws Exception{

        mockMvc.perform(MockMvcRequestBuilders
                .post(URL_CREATE_SUB_DEPT_EMPL)
                .header("Authorization", "Bearer " + getToken())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new SubDeptEmployee(
                     null, "Беркут", "Ярослав", "Юрьевич",
                         new Date(new GregorianCalendar(1975, Calendar.DECEMBER, 12).getTime().getTime()),
                "Серия: 24 12 Номер: 521214",
                         subDepartmentService.findByName("Департамент дошкольного образования")))))
                .andExpect(status().isCreated())
                .andDo(print());
    }

    @Test
    public  void updateSubDeptEmplTest() throws Exception{

        mockMvc.perform(MockMvcRequestBuilders
                .put(URL_UPDATE_SUB_DEPT_EMPL, "Кумиров", "Валентин", "Тигранович")
                .header("Authorization", "Bearer " + getToken())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new SubDeptEmployee(
                     null, "Кумиров", "Валентин", "Тигранович",
                         new Date(new GregorianCalendar(1981, Calendar.AUGUST, 13).getTime().getTime()),
                "Серия: 19 25 Номер: 582134",
                         subDepartmentService.findByName("Отдел обеспечения лекарственными средствами и медицинскими приборами")))))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void deleteSubDeptEmplTest() throws Exception
    {
        mockMvc.perform( MockMvcRequestBuilders
                .delete(URL_DELETE_SUB_DEPT_EMPL, "Беркут", "Ярослав", "Юрьевич")
                .header("Authorization", "Bearer " + getToken()))
                .andExpect(status().isNoContent());

        mockMvc.perform(MockMvcRequestBuilders
                .get(URL_FIND_ALL_SUB_DEPT_EMPL)
                .header("Authorization", "Bearer " + getToken())
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
    }
}
