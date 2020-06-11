package ru.d1soul.departments.service.authentification;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.d1soul.departments.api.repository.authentification.UserRepository;
import ru.d1soul.departments.api.service.authentification.UserService;
import ru.d1soul.departments.model.User;
import ru.d1soul.departments.web.exception.BadFormException;
import ru.d1soul.departments.web.exception.NotFoundException;
import java.util.List;

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
    public List<User> findAll(Sort sort) {
        return userRepository.findAll(sort);
    }

    @Transactional(readOnly = true)
    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(()-> {
            throw new NotFoundException("Пользователь с именем: " + username + " не обнаружен!");
        });
    }

    @Transactional(readOnly = true)
    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(()-> {
            throw new NotFoundException("Пользователь с е-мейл: " + email + " не обнаружен!");
        });
    }

    @Override
    public User save(User user) {

        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new BadFormException("Логин: " + user.getUsername() + " уже занят други пользователем");
        }
        else if (userRepository.findByEmail(user.getEmail()).isPresent()){
            throw new BadFormException("Е-мейл" + user.getEmail() + " уже занят другим пользователем");
        }
        else if (!user.getPassword().equals(user.getConfirmPassword())) {
            throw new BadFormException("Пароль и проверочный пароль не совпадают!");
        }
        else {
            user.setUsername(user.getUsername());
            user.setEmail(user.getEmail());
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            user.setConfirmPassword(bCryptPasswordEncoder.encode(user.getConfirmPassword()));
            user.setBirthDate(user.getBirthDate());
            user.setGender(user.getGender());
            user.setIsBanned(user.getIsBanned());
            user.setRoles(user.getRoles());
            return userRepository.save(user);
        }
    }

    @Override
    public User update(String username, User updUser) {
        return userRepository.findByUsername(username).map(user -> {
            if (userRepository.findByUsernameAndIdNot(updUser.getUsername(), user.getId()).isPresent()){
                throw new BadFormException("Логин: " + updUser.getUsername() + " уже занят други пользователем");
            }
            else  if (userRepository.findByEmailAndIdNot(updUser.getEmail(), user.getId()).isPresent()){
                throw new BadFormException("Е-мейл" + updUser.getEmail() + " уже занят другим пользователем");
            }
            else {
                user.setUsername(updUser.getUsername());
                user.setEmail(updUser.getEmail());
                user.setBirthDate(updUser.getBirthDate());
                user.setGender(updUser.getGender());
                user.setIsBanned(updUser.getIsBanned());
                user.setRoles(updUser.getRoles());
                return userRepository.save(user);
            }
        }).orElseThrow(()->{
            throw new NotFoundException("Пользователь с именем: " + updUser.getUsername() + " не обнаружен!");
        });
    }

    @Override
    public User changePassword(String username,
                               String oldPassword,
                               String newPassword,
                               String newConfirmPassword ){
       return userRepository.findByUsername(username).map(user -> {
           if (!bCryptPasswordEncoder.matches(oldPassword, user.getPassword())) {
               throw new BadFormException("Старый пароль введён неверно!");
           }
           else if (!newPassword.equals(newConfirmPassword)) {
               throw new BadFormException("Новый пароль и проверочный пароль не совпадают!");
           }
           else {
               user.setPassword(bCryptPasswordEncoder.encode(newPassword));
               user.setConfirmPassword(bCryptPasswordEncoder.encode(newConfirmPassword));
               return userRepository.save(user);
           }
       }).orElseThrow(()-> {
           throw new NotFoundException("Пользователь не найден!");
       });
    }

    @Override
    public User resetForgottenPassword(String username, User user) {
        return userRepository.findByUsername(username).map(changingPasswordUser ->{
            changingPasswordUser.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            changingPasswordUser.setConfirmPassword(bCryptPasswordEncoder.encode(user.getConfirmPassword()));
            return changingPasswordUser;
        }).orElseThrow(()-> {
            throw new NotFoundException("Пользователь не найден!");
        });
    }

    @Override
    public void deleteByUsername(String username) {
        userRepository.deleteByUsername(username);
    }
}