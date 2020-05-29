package ru.d1soul.departments.service.authentification;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.d1soul.departments.api.repository.authentification.UserRepository;
import ru.d1soul.departments.api.service.authentification.UserService;
import ru.d1soul.departments.model.User;
import ru.d1soul.departments.web.exception.BadFormException;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
public class UserServiceImpl implements UserService {

    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User save(User user) {
        user.setUsername(user.getUsername());
        user.setEmail(user.getEmail());
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setConfirmPassword(bCryptPasswordEncoder.encode(user.getConfirmPassword()));
        user.setBirthDate(user.getBirthDate());
        user.setGender(user.getGender());
        user.setRoles(user.getRoles());
        return userRepository.save(user);
    }

    @Override
    public User changePassword(String username,
                               String oldPassword,
                               String newPassword,
                               String newConfirmPassword ){
       return userRepository.findByUsername(username).map(user -> {
           if ( bCryptPasswordEncoder.matches(oldPassword, user.getPassword())){
               user.setPassword(bCryptPasswordEncoder.encode(newPassword));
               user.setConfirmPassword(bCryptPasswordEncoder.encode(newConfirmPassword));
              return userRepository.save(user);
           } else {
               throw new BadFormException("Старый пароль введён неверно!");
           }
       }).get();
    }

    @Override
    public void deleteByUsername(String username) {
        userRepository.deleteByUsername(username);
    }
}
