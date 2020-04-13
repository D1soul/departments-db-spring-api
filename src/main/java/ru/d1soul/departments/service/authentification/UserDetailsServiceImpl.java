package ru.d1soul.departments.service.authentification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.d1soul.departments.api.service.authentification.UserService;
import ru.d1soul.departments.model.User;
import ru.d1soul.departments.security.jwt.dto.JwtUserDetails;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private UserService userService;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserDetailsServiceImpl(UserService userService,
                                  BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userService = userService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    public void saveUser(User user) {
        User newUser = new User();
        newUser.setUsername(user.getUsername());
        newUser.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        newUser.setConfirmPassword(bCryptPasswordEncoder.encode(user.getConfirmPassword()));
        newUser.setBirthDate(user.getBirthDate());
        newUser.setGender(user.getGender());
        newUser.setRoles(user.getRoles());
        userService.save(user);
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findByUsername(username).orElseThrow(() ->
                new UsernameNotFoundException("User Not Found with -> username or email : " + username)
        );
        return JwtUserDetails.createUser(user);
    }
}
