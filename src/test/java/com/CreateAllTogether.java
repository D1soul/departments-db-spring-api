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
import ru.d1soul.departments.api.service.department.MainDepartmentService;
import ru.d1soul.departments.api.service.department.SubDepartmentService;
import ru.d1soul.departments.model.MainDepartment;
import ru.d1soul.departments.model.MainDeptEmployee;
import ru.d1soul.departments.model.SubDepartment;
import ru.d1soul.departments.model.SubDeptEmployee;
import java.util.Date;
import java.util.GregorianCalendar;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = SpringRestApiRunner.class)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application.yaml")
public class CreateAllTogether {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MainDepartmentService mainDepartmentService;

    @Autowired
    private SubDepartmentService subDepartmentService;

    private static final String URL_FIND_ALL_MAIN_DEPARTMENTS = "http://localhost:8080/departments-app/main_departments";
    private static final String URL_CREATE_MAIN_DEP = "http://localhost:8080/departments-app/main_departments/";
    private static final String URL_CREATE_MAIN_DEPT_EMPL = "http://localhost:8080/departments-app/main_dept_employees";
    private static final String URL_CREATE_SUB_DEP = "http://localhost:8080/departments-app/sub-departments";
    private static final String URL_CREATE_SUB_DEPT_EMPL = "http://localhost:8080/departments-app/sub-dept_employees";

    @Test
    public  void createMainDeptTest() throws Exception{

        mockMvc.perform(MockMvcRequestBuilders
                .post(URL_CREATE_MAIN_DEP)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new MainDepartment(
                  "Департамент культуры"))));

        mockMvc.perform(MockMvcRequestBuilders
                .post(URL_CREATE_MAIN_DEPT_EMPL)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new MainDeptEmployee(
                     null, "Калинин", "Тимур", "Васильевич",
                         new Date(new GregorianCalendar(1951, 4, 11).getTime().getTime()),
                "Серия: 31 42 Номер: 963214",
                         mainDepartmentService.findByName("Департамент культуры").get()))));

        mockMvc.perform(MockMvcRequestBuilders
                .post(URL_CREATE_SUB_DEP)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new SubDepartment(
                        "Департамент рока",
                        mainDepartmentService.findByName("Департамент культуры").get()))));

        mockMvc.perform(MockMvcRequestBuilders
                .post(URL_CREATE_SUB_DEPT_EMPL)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new SubDeptEmployee(
                     null, "Грачёва", "Наталья", "Юрьевна",
                         new Date(new GregorianCalendar(1978, 9, 19).getTime().getTime()),
                "Серия: 64 14 Номер: 723214",
                         subDepartmentService.findByName("Департамент рока").get()))));

        mockMvc.perform(MockMvcRequestBuilders
                .post(URL_CREATE_SUB_DEPT_EMPL)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new SubDeptEmployee(
                        null, "Брежнева", "Наталья", "Валерьевна",
                        new Date(new GregorianCalendar(1979, 5, 11).getTime().getTime()),
                        "Серия: 62 34 Номер: 762514",
                        subDepartmentService.findByName("Департамент рока").get()))));

        mockMvc.perform(MockMvcRequestBuilders
                .get(URL_FIND_ALL_MAIN_DEPARTMENTS)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }
}
