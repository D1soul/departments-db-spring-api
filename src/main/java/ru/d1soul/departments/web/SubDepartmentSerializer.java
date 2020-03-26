package ru.d1soul.departments.web;

import ru.d1soul.departments.model.SubDepartment;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;

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
