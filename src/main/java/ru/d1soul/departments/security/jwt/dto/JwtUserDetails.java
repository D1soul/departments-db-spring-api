package ru.d1soul.departments.security.jwt.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.d1soul.departments.model.User;
import ru.d1soul.departments.model.Role;
import java.util.*;
import java.util.stream.Collectors;

@Getter
@Setter
@RequiredArgsConstructor
public class JwtUserDetails implements UserDetails {

    private User user;
    private final Long id;
    private final String username;
    private final String password;
    private final String confirmPassword;
    private final Date birthDate;
    private final String gender;
    private final Collection<? extends GrantedAuthority> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getRoles().stream().map(role ->
            new SimpleGrantedAuthority(role.getRole())).collect(Collectors.toList());
    }

    public static JwtUserDetails createUser(User user) {
        Set<GrantedAuthority> authorities = getAuthoritiesSet(user.getRoles());

        return new JwtUserDetails(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                user.getConfirmPassword(),
                user.getBirthDate(),
                user.getGender(),
                authorities
        );
    }

    public static Set<GrantedAuthority> getAuthoritiesSet(Set<Role> roles){
        Set<GrantedAuthority> rolesSet = new HashSet<>();
        roles.forEach(role -> {
            rolesSet.add(new SimpleGrantedAuthority(role.getRole()));
        });
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
    public boolean isAccountNonLocked() {
        return true;
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
