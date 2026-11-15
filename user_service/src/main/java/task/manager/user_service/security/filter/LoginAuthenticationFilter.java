package task.manager.user_service.security.filter;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
public class LoginAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    @PostConstruct
    private void init() {
        this.setFilterProcessesUrl("/api/v1/login");
        this.setAuthenticationManager(authenticationManager);
    }
}