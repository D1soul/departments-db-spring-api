package ru.d1soul.departments.security.jwt.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "password_reset_token")
public class PasswordResetDto  {

    @NonNull
    @NotNull
    private String password;

    @NonNull
    @NotNull
    private String confirmPassword;

    @NonNull
    @NotNull
    private String token;
}
