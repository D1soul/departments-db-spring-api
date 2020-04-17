package ru.d1soul.departments.security.jwt.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthUser {
    private String username;
    private String password;
}
