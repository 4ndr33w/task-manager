package task.manager.user_service.data.dto.request;

import java.time.LocalDate;

public record UserRequestDto(
        String name,
        String lastName,
        String email,
        String phone,
        LocalDate birthDate,
        String password
) {
}