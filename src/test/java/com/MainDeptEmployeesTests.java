package com;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import ru.d1soul.departments.api.service.department.MainDepartmentService;
import ru.d1soul.departments.model.MainDeptEmployee;

import java.util.Date;
import java.util.GregorianCalendar;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = SpringRestApiRunner.class)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application.yaml")
public class MainDeptEmployeesTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    MainDepartmentService mainDepartmentService;


    private static final String URL_FIND_MAIN_DEPT_EMPL_BY_FULL_NAME =
            "http://localhost:8080/departments-app/main_dept_employees/{lastName}/{firstName}/{middleName}";
    private static final String URL_FIND_ALL_MAIN_DEPT_EMPL = "http://localhost:8080/departments-app/main_dept_employees";
    private static final String URL_UPDATE_MAIN_DEPT_EMPL =
            "http://localhost:8080/departments-app/main_dept_employees/{lastName}/{firstName}/{middleName}";
    private static final String URL_CREATE_MAIN_DEPT_EMPL = "http://localhost:8080/departments-app/main_dept_employees";
    private static final String URL_DELETE_MAIN_DEPT_EMPL =
            "http://localhost:8080/departments-app/main_dept_employees/{lastName}/{firstName}/{middleName}";

    @Test
    public void findAllMainDeptEmplTest() throws Exception
    {        mockMvc.perform(MockMvcRequestBuilders
                .get(URL_FIND_ALL_MAIN_DEPT_EMPL)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void findEmplByFullNameTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .get(URL_FIND_MAIN_DEPT_EMPL_BY_FULL_NAME, "Колесникова", "Ольга", "Константиновна" )
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());
    }

    @Test
    public  void createMainDeptEmplTest() throws Exception{

        mockMvc.perform(MockMvcRequestBuilders
                .post(URL_CREATE_MAIN_DEPT_EMPL)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new MainDeptEmployee(
                        null, "Куличин", "Игнат", "Асанович",
                            new Date(new GregorianCalendar(1940, 8, 16).getTime().getTime()),
                   "Серия: 52 13 Номер: 653214",
                            mainDepartmentService.findByName("Департамент здравоохранения").get()))))
                .andExpect(status().isCreated())
                .andDo(print());
    }

    @Test
    public  void updateMainDeptEmplTest() throws Exception{

        mockMvc.perform(MockMvcRequestBuilders
                .put(URL_UPDATE_MAIN_DEPT_EMPL, "Дружинин", "Юрий", "Михайлович")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new MainDeptEmployee(
                     null, "Дружинин", "Юрий", "Михайлович",
                         new Date(new GregorianCalendar(1968, 6, 12).getTime().getTime()),
                "Серия: 26 42 Номер: 671315",
                            mainDepartmentService.findByName("Департамент образования и науки").get()))))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void deleteMainDeptEmplTest() throws Exception
    {
        mockMvc.perform( MockMvcRequestBuilders
                .delete(URL_DELETE_MAIN_DEPT_EMPL, "Куличин", "Игнат", "Асанович"))
                .andExpect(status().isNoContent());

        mockMvc.perform(MockMvcRequestBuilders
                .get(URL_FIND_ALL_MAIN_DEPT_EMPL)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
    }
}
