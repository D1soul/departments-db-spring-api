package ru.d1soul.departments.service.authentification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ru.d1soul.departments.api.service.authentification.UserService;
import ru.d1soul.departments.model.AuthUser;
import ru.d1soul.departments.model.Role;
import java.util.HashSet;
import java.util.Set;

@Configuration
@EnableWebSecurity
public class UserDetailsServiceImpl implements UserDetailsService {

    private UserService userService;

    @Autowired
    public UserDetailsServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AuthUser user = userService.findByUsername(username).get();

        if (user != null){
            Set<GrantedAuthority> authorities = grantedAuthorities(user.getRoles());
            return new User(user.getUsername(), user.getPassword(), authorities);
        }
        else {
            throw new UsernameNotFoundException("Username not found");
        }
    }

    public Set<GrantedAuthority> grantedAuthorities(Set<Role> roles){
        Set<GrantedAuthority> authorityRole = new HashSet<>();
        for (Role role: roles){
            authorityRole.add(new SimpleGrantedAuthority(role.getRole()));
        }
        return authorityRole;
    }
}
