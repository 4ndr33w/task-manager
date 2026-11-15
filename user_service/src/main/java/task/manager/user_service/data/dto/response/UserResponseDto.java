package task.manager.user_service.data.dto.response;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record UserResponseDto(
        UUID id,
        String name,
        String lastName,
        String email,
        String phone,
        LocalDate birthDate,
        Instant createdAt,
        Instant updatedAt
) {
}