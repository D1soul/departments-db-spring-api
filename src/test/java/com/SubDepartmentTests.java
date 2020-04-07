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
import ru.d1soul.departments.model.SubDepartment;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = SpringRestApiRunner.class)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application.yaml")
public class SubDepartmentTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MainDepartmentService mainDepartmentService;

    private static final String URL_FIND_ALL_SUB_DEP = "http://localhost:8080/departments-app/sub-departments";
    private static final String URL_FIND_SUB_DEP_BY_NAME = "http://localhost:8080/departments-app/sub-departments/{name}";
    private static final String URL_UPDATE_SUB_DEP = "http://localhost:8080/departments-app/sub-departments/{name}";
    private static final String URL_CREATE_SUB_DEP = "http://localhost:8080/departments-app/sub-departments";
    private static final String URL_DELETE_SUB_DEP = "http://localhost:8080/departments-app/sub-departments/{name}";

    @Test
    public void findAllSubDepTest() throws Exception
    {        mockMvc.perform(MockMvcRequestBuilders
                .get(URL_FIND_ALL_SUB_DEP)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void findSubDepByName() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders
                .get(URL_FIND_SUB_DEP_BY_NAME, "Департамент общего образования")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public  void createSubDepTest() throws Exception{

        mockMvc.perform(MockMvcRequestBuilders
                .post(URL_CREATE_SUB_DEP)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new SubDepartment(
                  "Отдел архитектурного облика и городской среды",
                         mainDepartmentService.findByName("Департамент архитектуры").get()))))
                .andExpect(status().isCreated())
                .andDo(print());
    }

    @Test
    public  void updateSubDepTest() throws Exception{

        mockMvc.perform(MockMvcRequestBuilders
                .put(URL_UPDATE_SUB_DEP, "Департамент информационного обеспечения градостроительства")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new SubDepartment(
                  "Департамент информационного обеспечения градостроительства",
                         mainDepartmentService.findByName("Департамент образования и науки").get()))))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void deleteMainEmplTest() throws Exception
    {
        mockMvc.perform( MockMvcRequestBuilders
                .delete(URL_DELETE_SUB_DEP, "Отдел архитектурного облика и городской среды"))
                .andExpect(status().isNoContent());

        mockMvc.perform(MockMvcRequestBuilders
                .get(URL_FIND_ALL_SUB_DEP)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
    }
}
