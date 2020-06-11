package ru.d1soul.departments.security.jwt.dto;

import lombok.*;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class AuthUser {

    @NonNull
    @NotNull
    private String username;

    @NonNull
    @NotNull
    private String password;
}
