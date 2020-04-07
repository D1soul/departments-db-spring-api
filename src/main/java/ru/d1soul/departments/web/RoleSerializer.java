package ru.d1soul.departments.web;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import ru.d1soul.departments.model.MainDepartment;
import ru.d1soul.departments.model.Role;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


public class RoleSerializer extends StdSerializer<Role> {

    public RoleSerializer() {
        this(Role.class);
    }

    public RoleSerializer(Class<Role> t) {
        super(t);
    }

    @Override
    public void serialize(
            Role role,
            JsonGenerator generator,
            SerializerProvider provider)
            throws IOException {

        String name = role.getRole();

        generator.writeObject(name);
    }




}
