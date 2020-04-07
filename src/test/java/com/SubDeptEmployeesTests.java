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
import ru.d1soul.departments.api.service.department.SubDepartmentService;
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
public class SubDeptEmployeesTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

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

    @Test
    public void findAllSubDeptEmplsTest() throws Exception
    {        mockMvc.perform(MockMvcRequestBuilders
                .get(URL_FIND_ALL_SUB_DEPT_EMPL)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void findSubDeptEmplByIdTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .get(URL_FIND_SUB_DEPT_EMPL_BY_FULL_NAME, "Кулибин", "Алексей", "Тимурович")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public  void createSubDeptEmplTest() throws Exception{

        mockMvc.perform(MockMvcRequestBuilders
                .post(URL_CREATE_SUB_DEPT_EMPL)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new SubDeptEmployee(
                     null, "Беркут", "Ярослав", "Юрьевич",
                         new Date(new GregorianCalendar(1975, 11, 12).getTime().getTime()),
                "Серия: 24 12 Номер: 521214",
                         subDepartmentService.findByName("Департамент дошкольного образования").get()))))
                .andExpect(status().isCreated())
                .andDo(print());
    }

    @Test
    public  void updateSubDeptEmplTest() throws Exception{

        mockMvc.perform(MockMvcRequestBuilders
                .put(URL_UPDATE_SUB_DEPT_EMPL, "Кумиров", "Валентин", "Тигранович")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new SubDeptEmployee(
                     null, "Кумиров", "Валентин", "Тигранович",
                         new Date(new GregorianCalendar(1981, 7, 13).getTime().getTime()),
                "Серия: 19 25 Номер: 582134",
                         subDepartmentService.findByName("Отдел обеспечения лекарственными средствами и медицинскими приборами").get()))))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void deleteSubDeptEmplTest() throws Exception
    {
        mockMvc.perform( MockMvcRequestBuilders
                .delete(URL_DELETE_SUB_DEPT_EMPL, "Беркут", "Ярослав", "Юрьевич"))
                .andExpect(status().isNoContent());

        mockMvc.perform(MockMvcRequestBuilders
                .get(URL_FIND_ALL_SUB_DEPT_EMPL)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
    }
}
