package task.manager.user_service.security.component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import task.manager.user_service.data.enums.RsaKeyType;
import task.manager.user_service.exception.KeyReaderException;
import task.manager.user_service.properties.RsaKeyProperties;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.interfaces.RSAKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Slf4j
@Component
@RequiredArgsConstructor
public class KeyProvider {
    private static final String PUBLIC_KEY_PREFIX = "-----BEGIN PUBLIC KEY-----";
    private static final String PUBLIC_KEY_POSTFIX = "-----END PUBLIC KEY-----";
    private static final String PRIVATE_KEY_PREFIX = "-----BEGIN PRIVATE KEY-----";
    private static final String PRIVATE_KEY_POSTFIX = "-----END PRIVATE KEY-----";
    private static final String RSA_ALGORITHM_KEY = "RSA";

    private final ResourceLoader resourceLoader;
    private final RsaKeyProperties keyProperties;

    public RSAKey getRsaKey(RsaKeyType rsaKeyType) {
        try {
            String key = getFileContent(rsaKeyType);
            byte[] decodedKey = Base64.getDecoder().decode(key);
            EncodedKeySpec keySpec = getKeySpec(decodedKey, rsaKeyType);

            return switch (rsaKeyType) {
                case PUBLIC -> (RSAKey) getKeyFactory().generatePublic(keySpec);
                default -> (RSAKey) getKeyFactory().generatePrivate(keySpec);
            };
        }
        catch (Exception e) {
            log.error("Ошибка создания спецификации ключа: {}", e.getMessage(), e);
            throw new KeyReaderException("Ошибка создания спецификации ключа");
        }
    }

    private EncodedKeySpec getKeySpec(byte[] decodedKey, RsaKeyType rsaKeyType) {
        return switch (rsaKeyType) {
            case PUBLIC -> new X509EncodedKeySpec(decodedKey);
            default -> new PKCS8EncodedKeySpec(decodedKey);
        };
    }

    private String getFileContent(RsaKeyType rsaKeyType) {
        return switch (rsaKeyType) {
            case PUBLIC -> readFile(keyProperties.getPublicKeyPath())
                    .replace(PUBLIC_KEY_PREFIX, "")
                    .replace(PUBLIC_KEY_POSTFIX, "")
                    .replaceAll("\r\n", "");
            default -> readFile(keyProperties.getPrivateKeyPath())
                    .replace(PRIVATE_KEY_PREFIX, "")
                    .replace(PRIVATE_KEY_POSTFIX, "")
                    .replaceAll("\r\n", "");
        };
    }

    private KeyFactory getKeyFactory() {
        try {
            return KeyFactory.getInstance(RSA_ALGORITHM_KEY);
        }
        catch (Exception e) {
            log.error("Ошибка выбора алгоритма для фабрики ключей: {}", e.getMessage(), e);
            throw new KeyReaderException("Ошибка выбора алгоритма для фабрики ключей");
        }
    }

    private String readFile(String filePath) {
        Resource resource = resourceLoader.getResource(filePath);
        try(InputStream stream = resource.getInputStream()) {
            return new String(stream.readAllBytes(), StandardCharsets.UTF_8);
        }
        catch (Exception e) {
            log.error("Ошибка чтения файла ключа: {}", e.getMessage(), e);
            throw new KeyReaderException("Ошибка чтения файла ключа");
        }
    }
}