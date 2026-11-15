package task.manager.user_service.security.component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import task.manager.user_service.properties.JwtProperties;
import task.manager.user_service.security.details.AppUserDetails;
import task.manager.user_service.utils.constant.ClaimKey;

import java.time.Instant;
import java.util.UUID;

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
}