package ru.d1soul.departments.service.authentification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.d1soul.departments.api.repository.authentification.UserRepository;
import ru.d1soul.departments.api.service.authentification.UserService;
import ru.d1soul.departments.model.AuthUser;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<AuthUser> findAll() {
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<AuthUser> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public AuthUser save(AuthUser user) {
        return userRepository.save(user);
    }

    @Override
    public void deleteByUsername(String username) {
        userRepository.deleteByUsername(username);
    }
}
