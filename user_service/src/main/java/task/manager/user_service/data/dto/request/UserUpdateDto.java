package task.manager.user_service.data.dto.request;

import java.time.LocalDate;

public record UserUpdateDto(
        String name,
        String lastName,
        String phone,
        LocalDate birthDate
) {
}