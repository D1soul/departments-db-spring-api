package ru.d1soul.web.service;

import ru.d1soul.api.service.MainDepartmentService;
import ru.d1soul.model.MainDepartment;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;

@JsonComponent
public class MainDepartmentDeserializer extends JsonDeserializer<MainDepartment> {

    private MainDepartmentService mainDepartmentService;

    public MainDepartmentDeserializer(){
    }

    @Autowired
    public MainDepartmentDeserializer(MainDepartmentService mainDepartmentService){
        this.mainDepartmentService = mainDepartmentService;
    }

    @Override
    public MainDepartment deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {

        String name = jsonParser.getValueAsString();

        MainDepartment mainDepartment = mainDepartmentService.findByName(name).get();

        return mainDepartment;
    }
}