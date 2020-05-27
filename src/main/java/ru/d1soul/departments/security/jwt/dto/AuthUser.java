package ru.d1soul.departments.security.jwt.dto;

import lombok.*;

@Data
@AllArgsConstructor
public class AuthUser {
    private String username;
    private String password;
}
