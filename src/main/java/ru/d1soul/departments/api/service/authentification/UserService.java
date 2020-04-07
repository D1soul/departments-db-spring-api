package ru.d1soul.departments.api.service.authentification;

import ru.d1soul.departments.model.AuthUser;

import java.util.Optional;

public interface UserService {
   Optional<AuthUser> findByUserName(String userName);
   AuthUser save(AuthUser authUser);
}

