package ru.d1soul.departments.service.authentification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.d1soul.departments.api.repository.authentification.UserRepository;
import ru.d1soul.departments.security.jwt.dto.JwtUserDetails;
import ru.d1soul.departments.web.exception.BadFormException;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private UserRepository userRepository;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userService) {
        this.userRepository = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return JwtUserDetails.createUser(userRepository.findByUsername(username).orElseThrow(()-> {
            throw new BadFormException("Пользователь с таким именем не найден!");
        }));
    }
}
