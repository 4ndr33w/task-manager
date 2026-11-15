package task.manager.user_service.security.component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import task.manager.user_service.exception.AuthorizationException;
import task.manager.user_service.properties.JwtProperties;
import task.manager.user_service.security.details.AppUserDetails;
import task.manager.user_service.utils.constant.ClaimKey;

import java.time.Instant;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final JwtProperties properties;

    public String buildToken(AppUserDetails userDetails) {
        return JWT.create()
                .withIssuer(properties.getIssuer())
                .withSubject(userDetails.getUsername())
                .withAudience(properties.getAudience())
                .withClaim(ClaimKey.ROLES, userDetails.getUser().getRoles().stream().map(Enum::name).toList())
                .withIssuedAt(Instant.now())
                .withExpiresAt(Instant.now().plusSeconds(properties.getTokenLifeTimeSeconds()))
                .withJWTId(UUID.randomUUID().toString())
                .withClaim(ClaimKey.USER_ID, String.valueOf(userDetails.getUser().getId()))
                .sign(Algorithm.HMAC256(properties.getSecret()));
    }

    public DecodedJWT decodeAccessToken(String token) {
        try {
            return verifyToken().verify(token);
        }
        catch (Exception e) {
            log.warn("Не удалось верифицировать и декодировать токен: {}", e.getMessage(), e);
            throw new AuthorizationException("Не удалось верифицировать и декодировать токен");
        }
    }

    private JWTVerifier verifyToken() {
        return JWT.require(Algorithm.HMAC256(properties.getSecret()))
                .withIssuer(properties.getIssuer())
                .build();
    }
}