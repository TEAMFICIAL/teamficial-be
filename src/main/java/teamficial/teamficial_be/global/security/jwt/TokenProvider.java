package teamficial.teamficial_be.global.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import teamficial.teamficial_be.domain.user.entity.User;
import teamficial.teamficial_be.global.dto.TokenResponseDTO;

import java.security.Key;
import java.util.Date;


@Component
@RequiredArgsConstructor
public class TokenProvider implements InitializingBean {

    @Value("${jwt.secret}")
    private String secretKey;
    private Key key;

    @Value("${jwt.token.access-expiration-time}")
    private long accessExpirationTime;

    @Value("${jwt.token.refresh-expiration-time}")
    private long refreshExpirationTime;

    private final UserDetailsService userDetailsService;

    @Override
    public void afterPropertiesSet(){
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String createAccessToken(User user) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + accessExpirationTime);

        return Jwts.builder()
                .setSubject(String.valueOf(user.getId()))
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    public String createRefreshToken(User user) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + refreshExpirationTime);

        return Jwts.builder()
                .setSubject(String.valueOf(user.getId()))
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(key,SignatureAlgorithm.HS256)
                .compact();
    }

    public TokenResponseDTO.RefreshTokenResponseDto recreate(User user, String refreshToken) {
        String accessToken = createAccessToken(user);

        if(getExpirationTime(refreshToken) <= getExpirationTime(accessToken)) {
            refreshToken = createRefreshToken(user);
        }

        return TokenResponseDTO.RefreshTokenResponseDto.of(user.getId(),accessToken,refreshToken);
    }

    public Long getExpirationTime(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration()
                .getTime();
    }

    public String getAccessToken(HttpServletRequest request){
        String token = request.getHeader("Authorization");
        if (StringUtils.hasText(token) && token.startsWith("Bearer ")) {
            return token.substring(7);
        }
        return null;
    }

    //userId 추출
    public String getUserIdFromToken(String token){
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    public Authentication getAuthentication(String token){
        UserDetails userDetails =
                (UserDetails)
                        userDetailsService.loadUserByUsername(getUserIdFromToken(token));
        return new UsernamePasswordAuthenticationToken(
                userDetails, token, userDetails.getAuthorities());
    }

    public boolean validateToken(String token){
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException ex) {
            throw new IllegalArgumentException("Invalid JWT signature or token malformed: "+ ex.getMessage());
        } catch (ExpiredJwtException ex) {
            throw new IllegalArgumentException("Expired JWT token: "+ ex.getMessage());
        } catch (UnsupportedJwtException ex) {
            throw new IllegalArgumentException("Unsupported JWT token: "+ ex.getMessage());
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("JWT token compact of null or empty: "+ ex.getMessage());
        }
    }

}
