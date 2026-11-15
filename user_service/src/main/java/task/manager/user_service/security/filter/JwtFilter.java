package task.manager.user_service.security.filter;

import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import task.manager.user_service.data.enums.UserRole;
import task.manager.user_service.security.component.JwtTokenProvider;
import task.manager.user_service.utils.constant.AuthorizationType;
import task.manager.user_service.utils.constant.ClaimKey;

import java.util.Collection;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder
            .getContextHolderStrategy();

    private final JwtTokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)  {
        SecurityContext context = this.securityContextHolderStrategy.createEmptyContext();
        try {
            String token = getTokenFromRequest(request);
            if(token == null) {
                filterChain.doFilter(request, response);
                return;
            }
            DecodedJWT decodedToken = tokenProvider.decodeAccessToken(token);
            String subject = decodedToken.getSubject();
            UUID userId = UUID.fromString(decodedToken.getClaim(ClaimKey.USER_ID).asString());
            Collection<? extends GrantedAuthority> roles = decodedToken.getClaim(ClaimKey.ROLES).asList(UserRole.class);

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(subject, userId, roles);

            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            context.setAuthentication(authentication);
            this.securityContextHolderStrategy.setContext(context);

            filterChain.doFilter(request, response);
        }
        catch (Exception e) {
            this.securityContextHolderStrategy.clearContext();
            log.error("Ошибка при попытке аутенфицировать пользоватлея по JWT-токену: {}", e.getMessage(), e);
        }
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader != null && authHeader.startsWith(AuthorizationType.BEARER)) {
            return authHeader.substring(AuthorizationType.BEARER.length() + 1);
        }
        return  null;
    }
}
