package ru.d1soul.departments.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import ru.d1soul.departments.model.Role;
import ru.d1soul.departments.service.authentification.UserDetailsServiceImpl;

import javax.annotation.PostConstruct;
import java.security.Key;
import java.util.Date;
import java.util.Set;

@Component
public class JwtTokenProvider {

    private UserDetailsServiceImpl userService;

    @Autowired
    public JwtTokenProvider( UserDetailsServiceImpl userService) {
        this.userService = userService;
    }

    @Value("{jwt.secretKey}")
    private String secretKey;

    @Value("${jwt.validityPeriod}")
    private long validityPeriod;

    @PostConstruct
    private Key decodeSecretKey() {
        byte[] keyBytes = Decoders.BASE64.decode(this.secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = this.userService.loadUserByUsername(token);
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String createToken(String username, Set<Role> roles){
        Claims claims  = Jwts.claims().setSubject(username);
        claims.put("roles", roles);

        Date currentTime = new Date();
        Date validityTime = new Date(currentTime.getTime() + validityPeriod);

        return Jwts.builder().setClaims(claims)
                             .setIssuedAt(currentTime)
                             .setExpiration(validityTime)
                             .signWith(decodeSecretKey(), SignatureAlgorithm.ES256)
                             .compact();
    }


    public String getUsername(String username) {
          return Jwts.parserBuilder().setSigningKey(decodeSecretKey())
                                     .build().parseClaimsJws(username)
                                     .getBody().getSubject();
    }
}
