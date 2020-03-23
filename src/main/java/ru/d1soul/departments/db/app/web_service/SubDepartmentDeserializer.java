package ru.d1soul.departments.db.app.web_service;

import ru.d1soul.departments.db.app.model.SubDepartment;
import ru.d1soul.departments.db.app.api.service.SubDepartmentService;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
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
    public SubDepartment deserialize(JsonParser jsonParser, DeserializationContext ctxt) throws IOException, JsonProcessingException {

        String name = jsonParser.getValueAsString();

        SubDepartment subDepartment = subDepartmentService.findByName(name).get();

        return subDepartment;
    }
}