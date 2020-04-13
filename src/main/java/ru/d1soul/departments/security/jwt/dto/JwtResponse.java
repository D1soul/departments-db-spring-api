package ru.d1soul.departments.security.jwt.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JwtResponse {
   private String username;
   private String token;
}
