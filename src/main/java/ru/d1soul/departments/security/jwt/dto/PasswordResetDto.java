package ru.d1soul.departments.security.jwt.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "password_reset_token")
public class PasswordResetDto  {
    @NotEmpty
    private String password;

    @NotEmpty
    private String confirmPassword;

    @NotEmpty
    private String token;
}
