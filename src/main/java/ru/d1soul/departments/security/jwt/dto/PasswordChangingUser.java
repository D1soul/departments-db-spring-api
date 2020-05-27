package ru.d1soul.departments.security.jwt.dto;

import lombok.*;

@Data
@AllArgsConstructor
public class PasswordChangingUser {
    private String username;
    private String password;
    private String newPassword;
    private String newConfirmPassword;
}
