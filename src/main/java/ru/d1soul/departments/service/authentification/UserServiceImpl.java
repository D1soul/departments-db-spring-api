package ru.d1soul.departments.service.authentification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.d1soul.departments.api.repository.authentification.UserRepository;
import ru.d1soul.departments.api.service.authentification.RoleService;
import ru.d1soul.departments.api.service.authentification.UserService;
import ru.d1soul.departments.model.Role;
import ru.d1soul.departments.model.User;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class UserServiceImpl implements UserService {
@Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private UserRepository userRepository;
    private RoleService roleService;

    @Autowired
    public UserServiceImpl(//BCryptPasswordEncoder bCryptPasswordEncoder,
                           UserRepository userRepository, RoleService roleService) {
      //  this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userRepository = userRepository;
        this.roleService = roleService;
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public User save(User user) {
        user.setUsername(user.getUsername());
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setConfirmPassword(bCryptPasswordEncoder.encode(user.getConfirmPassword()));
        user.setBirthDate(user.getBirthDate());
        user.setGender(user.getGender());
        Role role = roleService.findByRole("user");
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        user.setRoles(roles);
        return userRepository.save(user);
    }

    @Override
    public void deleteByUsername(String username) {
        userRepository.deleteByUsername(username);
    }
}
