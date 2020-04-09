package ru.d1soul.departments.api.service.authentification;

import ru.d1soul.departments.model.AuthUser;

import java.util.List;
import java.util.Optional;

public interface UserService {
   List<AuthUser> findAll();
   Optional<AuthUser> findByUsername(String username);
   AuthUser save(AuthUser authUser);
   void deleteByUsername(String username);
}

