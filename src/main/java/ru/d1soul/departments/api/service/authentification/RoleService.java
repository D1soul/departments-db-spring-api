package ru.d1soul.departments.api.service.authentification;

import ru.d1soul.departments.model.Role;
import java.util.Set;

public interface RoleService {
    Role findByRole(String role);
    Set<Role> findAllRoles();
}
