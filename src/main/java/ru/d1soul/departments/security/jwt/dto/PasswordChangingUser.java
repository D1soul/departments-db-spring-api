package ru.d1soul.departments.security.jwt.dto;

import lombok.*;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class PasswordChangingUser {

    @NonNull
    @NotNull
    private String username;

    @NonNull
    @NotNull
    private String password;

    @NonNull
    @NotNull
    private String newPassword;

    @NonNull
    @NotNull
    private String newConfirmPassword;
}
