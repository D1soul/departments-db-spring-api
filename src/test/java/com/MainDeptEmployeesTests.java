package com;

import ru.d1soul.departments.db.app.SpringRestApiRunner;
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
        String createEmpl = "{"
                          + "\"lastName\": \"Куличин\","
                          + "\"firstName\": \"Игнат\","
                          + "\"middleName\": \"Асанович\","
                          + "\"birthDate\": \"01/мая/1959\","
                          + "\"passport\": \"Серия: 52 13 Номер: 653214\","
                          + "\"mainDepartment\": \"Департамент здравоохранения\""
                          + "}";

        mockMvc.perform(MockMvcRequestBuilders
                .post(URL_CREATE_MAIN_DEPT_EMPL)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(createEmpl))
                .andExpect(status().isCreated())
                .andDo(print());
    }

    @Test
    public  void updateMainDeptEmplTest() throws Exception{
        String updateEmpl = "{"
                          + "\"lastName\": \"Кузьмин\","
                          + "\"firstName\": \"Алексей\","
                          + "\"middleName\": \"Александрович\","
                          + "\"birthDate\": \"05/ноября/1949\","
                          + "\"passport\": \"Серия: 12 43 Номер: 532214\","
                          + "\"mainDepartment\": \"Департамент здравоохранения\""
                          + "}";

        mockMvc.perform(MockMvcRequestBuilders
                .put(URL_UPDATE_MAIN_DEPT_EMPL, "Кузьмин", "Алексей", "Александрович")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(updateEmpl))
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
