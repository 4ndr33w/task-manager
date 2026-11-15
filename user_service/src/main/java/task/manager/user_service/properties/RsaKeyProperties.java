package task.manager.user_service.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "key-path")
public class RsaKeyProperties {
    private String privateKeyPath;
    private String publicKeyPath;
}