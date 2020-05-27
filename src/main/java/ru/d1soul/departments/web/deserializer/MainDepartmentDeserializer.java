package ru.d1soul.departments.web.deserializer;

import ru.d1soul.departments.api.service.department.MainDepartmentService;
import ru.d1soul.departments.model.MainDepartment;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;

public class MainDepartmentDeserializer extends JsonDeserializer<MainDepartment> {

    private MainDepartmentService mainDepartmentService;

    public MainDepartmentDeserializer(MainDepartmentService mainDepartmentService){
        this.mainDepartmentService = mainDepartmentService;
    }

    @Override
    public MainDepartment deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {

        String name = jsonParser.getValueAsString();

        return mainDepartmentService.findByName(name).orElseThrow();
    }
}