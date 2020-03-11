package com.departments_db_rest_api_angular.web_services;

import com.departments_db_rest_api_angular.entities.SubDepartment;
import com.departments_db_rest_api_angular.services.SubDepartmentService;
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