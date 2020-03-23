package com.departments_db_rest_api_angular.web_service;

import com.departments_db_rest_api_angular.entity.SubDepartment;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;

@JsonComponent
public class SubDepartmentSerializer extends StdSerializer<SubDepartment> {

    public SubDepartmentSerializer() {
        this(SubDepartment.class);
    }

    public SubDepartmentSerializer(Class<SubDepartment> t) {
        super(t);
    }

    @Override
    public void serialize(
            SubDepartment subDepartment,
            JsonGenerator generator,
            SerializerProvider provider)
            throws IOException {

        String name = subDepartment.getName();

        generator.writeObject(name);
    }
}
