package ru.d1soul.departments.security.jwt.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.Set;

@Data
@AllArgsConstructor
public class JwtResponse {
   private String username;
   private Set<String> roles;
   private String token;
}
