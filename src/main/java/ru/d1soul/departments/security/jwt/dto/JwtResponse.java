package ru.d1soul.departments.security.jwt.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import ru.d1soul.departments.model.Role;

import java.util.Collection;
import java.util.Set;

@Data
@AllArgsConstructor
public class JwtResponse {
   private String username;
   private Set<String> roles;
   private String token;
}
