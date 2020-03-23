package com.departments_db_rest_api_angular.web_service;

import com.departments_db_rest_api_angular.entity.MainDepartment;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;

@JsonComponent
public class MainDepartmentSerializer extends StdSerializer<MainDepartment> {

    public MainDepartmentSerializer() {
        this(MainDepartment.class);
    }

    public MainDepartmentSerializer(Class<MainDepartment> t) {
        super(t);
    }

    @Override
    public void serialize(
            MainDepartment mainDepartment,
            JsonGenerator generator,
            SerializerProvider provider)
            throws IOException {

        String name = mainDepartment.getName();

        generator.writeObject(name);
    }




}
