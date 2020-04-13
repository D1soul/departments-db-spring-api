package ru.d1soul.departments.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.d1soul.departments.security.jwt.dto.JwtUserDto;
import ru.d1soul.departments.service.authentification.UserDetailsServiceImpl;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Date;

@SuppressWarnings("rawtypes")
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

    public String createToken(JwtUserDto jwtUserDto){
        Claims claims  = Jwts.claims().setSubject(jwtUserDto.getUsername());
        claims.put("roles", jwtUserDto.getRoles());

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

    public String jwtRequestToken(HttpServletRequest request) {
        String jwtHeader = request.getHeader("Authorization");
        if (jwtHeader != null && jwtHeader.startsWith("Bearer ")) {
            return jwtHeader.substring(7);
        }
        return null;
    }

    public boolean validateToken(String jwtToken){
        try {
            Jwt<JwsHeader, Claims> claims =
                    Jwts.parserBuilder().setSigningKey(decodeSecretKey())
                                        .build().parseClaimsJws(jwtToken);
            if(claims.getBody().getExpiration().before(new Date())){
                return false;
            }
        }
        catch (JwtException ex){
            throw new JwtException("Expired or invalid JWT token");
        }
        return true;
    }
}