package task.manager.user_service.data.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.OffsetDateTime;

public record AuthenticationResponseDto(
        String token,
        @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
        OffsetDateTime expiredAt) {
}