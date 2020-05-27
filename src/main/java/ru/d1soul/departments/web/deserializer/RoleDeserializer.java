package ru.d1soul.departments.web.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import ru.d1soul.departments.api.service.authentification.RoleService;
import ru.d1soul.departments.model.Role;
import java.io.IOException;

public class RoleDeserializer extends JsonDeserializer<Role> {

    private RoleService roleService;

    public RoleDeserializer(RoleService roleService){
        this.roleService = roleService;
    }

    @Override
    public Role deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {

        String name = jsonParser.getValueAsString();
        return roleService.findByRole(name);
    }
}