package task.manager.user_service.service;

import task.manager.user_service.data.dto.request.UserRequestDto;
import task.manager.user_service.data.dto.request.UserUpdateDto;
import task.manager.user_service.data.dto.response.UserResponseDto;

import java.util.List;
import java.util.UUID;

public interface UserService {
    UserResponseDto create(UserRequestDto requestDto);
    UserResponseDto updateById(UserUpdateDto user, UUID id);
    UserResponseDto getById(UUID id);
    List<UserResponseDto> findAll(Integer page, Integer limit);
    void deleteById(UUID id);
}