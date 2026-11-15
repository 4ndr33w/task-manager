package task.manager.user_service.service;

import task.manager.user_service.data.dto.request.UserUpdateDto;
import task.manager.user_service.data.dto.response.UserResponseDto;

import java.util.UUID;

public interface AdminService {
    UserResponseDto updateById(UserUpdateDto user, UUID id);
    UserResponseDto getById(UUID id);
    void deleteById(UUID id);
}