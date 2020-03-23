package ru.d1soul.departments.db.app.web_service;

import ru.d1soul.departments.db.app.model.SubDepartment;
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
