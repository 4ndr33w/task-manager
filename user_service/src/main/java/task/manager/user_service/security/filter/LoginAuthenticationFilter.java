package task.manager.user_service.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import task.manager.user_service.data.dto.response.AuthenticationResponseDto;
import task.manager.user_service.exception.AuthorizationException;
import task.manager.user_service.properties.JwtProperties;
import task.manager.user_service.security.component.JwtTokenProvider;
import task.manager.user_service.security.details.AppUserDetails;
import task.manager.user_service.utils.constant.AuthorizationType;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Base64;

@Slf4j
@RequiredArgsConstructor
public class LoginAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final JwtProperties jwtProperties;
    private final ObjectMapper objectMapper;

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

    @Override
    public void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                         Authentication authResult) {
        AppUserDetails userDetails = (AppUserDetails) authResult.getPrincipal();
        String token = tokenProvider.buildToken(userDetails);

        printResponse(
                response,
                new AuthenticationResponseDto(
                        token,
                        OffsetDateTime.now().plusSeconds(jwtProperties.getTokenLifeTimeSeconds())));
    }

    private void printResponse(HttpServletResponse response, AuthenticationResponseDto responseDto) {
        try {
            String jsonResponse = objectMapper.writeValueAsString(responseDto);
            String authHeaderString = "%s %s".formatted(AuthorizationType.BEARER, responseDto.token());

            response.addHeader(HttpHeaders.AUTHORIZATION, authHeaderString);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().write(jsonResponse);
        }
        catch (Exception e) {
            log.error("Ошибка при попытке вернуть ответ с токуном при успешной авторизации по логину и  паролю {}", e.getMessage(), e);
            throw new AuthorizationException("Ошибка при попытке вернуть ответ с токуном при успешной авторизации по логину и  паролю");
        }
    }
}