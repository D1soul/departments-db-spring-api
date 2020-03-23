package com;

import ru.d1soul.SpringRestApiRunner;
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
    private static final String URL_FIND_ALL_MAIN_DEPARTMENTS = "http://localhost:8080/departments-app/main_departments";
    private static final String URL_CREATE_MAIN_DEP = "http://localhost:8080/departments-app/main_departments/";
    private static final String URL_CREATE_MAIN_DEPT_EMPL = "http://localhost:8080/departments-app/main_dept_employees";
    private static final String URL_CREATE_SUB_DEP = "http://localhost:8080/departments-app/sub_departments";
    private static final String URL_CREATE_SUB_DEPT_EMPL = "http://localhost:8080/departments-app/sub-dept_employees";

    @Test
    public  void createMainDeptTest() throws Exception{
        String createMainDep = "{"
                + "\"name\": \"Департамент культуры\""
                + "}";

        mockMvc.perform(MockMvcRequestBuilders
                .post(URL_CREATE_MAIN_DEP)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(createMainDep));

        String createMainDeptEmpl = "{"
                          + "\"lastName\": \"Куличин\","
                          + "\"firstName\": \"Игнат\","
                          + "\"middleName\": \"Асанович\","
                          + "\"birthDate\": \"01/марта/1959\","
                          + "\"passport\": \"Серия: 52 13 Номер: 653214\","
                          + "\"mainDepartment\": \"Департамент культуры\""
                          + "}";

        mockMvc.perform(MockMvcRequestBuilders
                .post(URL_CREATE_MAIN_DEPT_EMPL)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(createMainDeptEmpl));

        String createSubDept = "{"
                + "\"name\": \"Департамент музыки\","
                + "\"mainDepartment\": \"Департамент культуры\""
                + "}";

        mockMvc.perform(MockMvcRequestBuilders
                .post(URL_CREATE_SUB_DEP)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(createSubDept));

        String createSubDeptEmpl = "{"
                + "\"lastName\": \"Грачёва\","
                + "\"firstName\": \"Наталья\","
                + "\"middleName\": \"Юрьевна\","
                + "\"birthDate\": \"19/сентября/1978\","
                + "\"passport\": \"Серия: 64 14 Номер: 723214\","
                + "\"subDepartment\": \"Департамент музыки\""
                + "}";

        mockMvc.perform(MockMvcRequestBuilders
                .post(URL_CREATE_SUB_DEPT_EMPL)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(createSubDeptEmpl));

        mockMvc.perform(MockMvcRequestBuilders
                .get(URL_FIND_ALL_MAIN_DEPARTMENTS)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }
}
