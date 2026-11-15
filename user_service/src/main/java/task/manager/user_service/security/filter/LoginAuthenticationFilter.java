package task.manager.user_service.security.filter;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import task.manager.user_service.exception.AuthorizationException;
import task.manager.user_service.utils.constant.AuthorizationType;

import java.util.Base64;

@RequiredArgsConstructor
public class LoginAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    @PostConstruct
    private void init() {
        this.setFilterProcessesUrl("/api/v1/login");
        this.setAuthenticationManager(authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        String[] loginCredentials = extractCredentials(request);
        String login = loginCredentials[0];
        String password = loginCredentials[1];

        UsernamePasswordAuthenticationToken authRequest = UsernamePasswordAuthenticationToken.unauthenticated(login,
                password);
        setDetails(request, authRequest);
        return this.getAuthenticationManager().authenticate(authRequest);
    }

    private String[] extractCredentials(HttpServletRequest request) {
        try {
            String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

            if (authHeader.startsWith(AuthorizationType.BASIC)) {
                String credentials = new String((Base64.getDecoder().decode(authHeader.substring(6))));

                return credentials.split(":", 2);
            }
        }
        catch (Exception e) {
            throw new AuthorizationException("Не удалось извлечь заголовок базовой авторизации из запроса");
        }
        return null;
    }
}