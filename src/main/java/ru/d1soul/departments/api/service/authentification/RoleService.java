package ru.d1soul.departments.api.service.authentification;

import ru.d1soul.departments.model.Role;

public interface RoleService {
    Role findByRole(String role);
}
