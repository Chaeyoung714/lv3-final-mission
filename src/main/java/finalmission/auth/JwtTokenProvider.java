package finalmission.auth;

import finalmission.entity.Member;
import finalmission.exception.UnauthorizedException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String key;
    private SecretKey secretKey;

    @PostConstruct
    public void init() {
        secretKey = Keys.hmacShaKeyFor(key.getBytes(StandardCharsets.UTF_8));
    }

    public String createToken(Member member) {
        return Jwts.builder()
                .setSubject(member.getId().toString())
                .claim("name", member.getName())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .signWith(secretKey)
                .compact();
    }

    public Long extractId(String token) {
        try {
            String stringId = parseClaims(token).getSubject();
            return Long.parseLong(stringId);
        } catch (NumberFormatException e) {
            throw new UnauthorizedException("잘못된 토큰입니다.");
        }
    }

    private Claims parseClaims(String token) {
        try {
            Jws<Claims> jws = Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token);
            Claims claims = jws.getPayload();
            if (claims.getExpiration().before(new Date())) {
                throw new UnauthorizedException("토큰이 만료되었습니다.");
            }
            return claims;
        } catch (JwtException | IllegalArgumentException e) {
            throw new UnauthorizedException("유효하지 않은 토큰입니다.");
        }
    }
}
