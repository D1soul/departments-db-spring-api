package ru.d1soul.departments.security.jwt.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.Set;

@Data
@AllArgsConstructor
public class JwtUserDto {
    private String username;
    private String password;
    private Set<String> roles;
}
