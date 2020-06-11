package ru.d1soul.departments.security.jwt.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
@AllArgsConstructor
public class JwtUserDto {

    @NonNull
    @NotNull
    private String username;

    @NonNull
    @NotNull
    private String password;

    @NonNull
    @NotNull
    private Set<String> roles;
}
