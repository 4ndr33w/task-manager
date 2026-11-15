package task.manager.user_service.data.enums;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@RequiredArgsConstructor
public enum UserRole implements GrantedAuthority {
    USER("USER"),
    ADMIN("ADMIN");

    private final String value;

    @Override
    public String getAuthority() {
        return this.value;
    }
}