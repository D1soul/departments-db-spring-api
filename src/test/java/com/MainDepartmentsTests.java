package com;

import com.departments_db_rest_api_angular.SpringRestApiRunner;
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
public class MainDepartmentsTests {

    @Autowired
    private MockMvc mockMvc;
    private static final String URL_FIND_ALL_MAIN_DEPARTMENTS = "http://localhost:8080/departments-app/main_departments";
    private static final String URL_FIND_MAIN_DEP_BY_NAME = "http://localhost:8080/departments-app/main_departments/{name}";
    private static final String URL_UPDATE_MAIN_DEP = "http://localhost:8080/departments-app/main_departments/{name}";
    private static final String URL_CREATE_MAIN_DEP = "http://localhost:8080/departments-app/main_departments/";
    private static final String URL_DELETE_MAIN_DEP = "http://localhost:8080/departments-app/main_departments/{name}";

    @Test
    public void getAllMainDepTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .get(URL_FIND_ALL_MAIN_DEPARTMENTS)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void findMaiDepByName() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders
                .get(URL_FIND_MAIN_DEP_BY_NAME, "Департамент архитектуры")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public  void updateMainDepTest() throws Exception{
        String updateMainDep = "{"
                             +  "\"name\": \"Департамент архитектуры и строительства\""
                             +  "}";

        mockMvc.perform(MockMvcRequestBuilders
                .put(URL_UPDATE_MAIN_DEP, "Департамент архитектуры")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(updateMainDep))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public  void createMainDepTest() throws Exception{
        String createMainDep = "{"
                             + "\"name\": \"Департамент сельского хозяйства\""
                             + "}";

        mockMvc.perform(MockMvcRequestBuilders
                .post(URL_CREATE_MAIN_DEP)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(createMainDep))
                .andExpect(status().isCreated())
                .andDo(print());
    }

    @Test
    public void deleteMainDepTest() throws Exception
    {
        mockMvc.perform( MockMvcRequestBuilders
                .delete(URL_DELETE_MAIN_DEP, "Департамент сельского хозяйства") )
                .andExpect(status().isNoContent());

        mockMvc.perform(MockMvcRequestBuilders
                .get(URL_FIND_ALL_MAIN_DEPARTMENTS)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

}