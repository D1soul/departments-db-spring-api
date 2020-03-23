package ru.d1soul.departments.web.service;

import ru.d1soul.departments.api.service.SubDepartmentService;
import ru.d1soul.departments.model.SubDepartment;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;

@JsonComponent
public class SubDepartmentDeserializer extends JsonDeserializer<SubDepartment> {

    public SubDepartmentDeserializer(){
    }

    private SubDepartmentService subDepartmentService;

    @Autowired
    public SubDepartmentDeserializer(SubDepartmentService subDepartmentService){
        this.subDepartmentService = subDepartmentService;
    }

    @Override
    public SubDepartment deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {

        String name = jsonParser.getValueAsString();

        SubDepartment subDepartment = subDepartmentService.findByName(name).get();

        return subDepartment;
    }
}