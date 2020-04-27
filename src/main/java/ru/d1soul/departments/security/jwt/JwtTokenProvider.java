package ru.d1soul.departments.security.jwt;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import ru.d1soul.departments.security.jwt.dto.JwtUserDto;
import ru.d1soul.departments.service.authentification.UserDetailsServiceImpl;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

@SuppressWarnings("rawtypes")
@Component
public class JwtTokenProvider {

    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    public JwtTokenProvider( UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Value("{jwt.secretKey}")
    private String secretKey;

    @Value("${jwt.validityPeriod}")
    private long validityPeriod;

    @PostConstruct
    protected void initSecretKey() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public UsernamePasswordAuthenticationToken getAuthentication(String jwtUsername) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(jwtUsername);

         return new UsernamePasswordAuthenticationToken(userDetails,
                null, userDetails.getAuthorities());
    }

    public String createToken(JwtUserDto jwtUserDto){
        Claims claims  = Jwts.claims().setSubject(jwtUserDto.getUsername());
        claims.put("roles", jwtUserDto.getRoles());

        Date currentTime = new Date();
        Date validityTime = new Date(currentTime.getTime() + validityPeriod);

        return Jwts.builder().setClaims(claims)
                .setIssuedAt(currentTime)
                .setExpiration(validityTime)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public String getUsername(String username) {
        return Jwts.parser().setSigningKey(secretKey)
                .parseClaimsJws(username)
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
                    Jwts.parser().setSigningKey(secretKey)
                            .parseClaimsJws(jwtToken);
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
