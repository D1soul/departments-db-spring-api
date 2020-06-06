package ru.d1soul.departments.security.jwt.dto;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.d1soul.departments.model.User;
import ru.d1soul.departments.model.Role;
import java.util.*;

@Data
public class JwtUserDetails implements UserDetails {

    private User user;
    private final Long id;
    private final String username;
    private final String email;
    private final String password;
    private final String confirmPassword;
    private final Date birthDate;
    private final String gender;
    private final Boolean isBanned;
    private final Collection<? extends GrantedAuthority> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public static JwtUserDetails createUser(User user) {

        return new JwtUserDetails(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                user.getConfirmPassword(),
                user.getBirthDate(),
                user.getGender(),
                user.getIsBanned(),
                getAuthoritiesSet(new HashSet<>(user.getRoles()))
        );
    }

    public static Set<GrantedAuthority> getAuthoritiesSet(Set<Role> roles){
        Set<GrantedAuthority> rolesSet = new HashSet<>();
        roles.forEach(role -> rolesSet.add(new SimpleGrantedAuthority(role.getRole())));
        return rolesSet;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public  boolean isAccountNonLocked() {
        return !isBanned;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
