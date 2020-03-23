package com.departments_db_rest_api_angular.web_service;

import com.departments_db_rest_api_angular.entity.MainDepartment;
import com.departments_db_rest_api_angular.api.service.MainDepartmentService;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
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
    public MainDepartment deserialize(JsonParser jsonParser, DeserializationContext ctxt) throws IOException, JsonProcessingException {

        String name = jsonParser.getValueAsString();

        MainDepartment mainDepartment = mainDepartmentService.findByName(name).get();

        return mainDepartment;
    }
}