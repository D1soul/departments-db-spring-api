package ru.d1soul.departments.api.service.authentification;

import org.springframework.data.domain.Sort;
import ru.d1soul.departments.model.User;
import java.util.List;

public interface UserService {
   List<User> findAll(Sort sort);
   User findByUsername(String username);
   User findByEmail(String email);
   User save(User user);
   User update(String username, User user);
   void deleteByUsername(String username);
   User changePassword(String username, String oldPassword, String newPassword, String newConfirmPassword);
   User resetForgottenPassword(String username, User user);
}

