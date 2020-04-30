package ru.d1soul.departments.api.service.authentification;

import ru.d1soul.departments.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
   List<User> findAll();
   Optional<User> findByUsername(String username);
   User save(User user);
   void deleteByUsername(String username);
   User changePassword(String username, String oldPassword, String newPassword, String newConfirmPassword);
}

