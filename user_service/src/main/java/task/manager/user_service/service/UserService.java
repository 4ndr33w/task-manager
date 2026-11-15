package task.manager.user_service.service;

import task.manager.user_service.data.dto.request.UserRequestDto;
import task.manager.user_service.data.dto.request.UserUpdateDto;
import task.manager.user_service.data.dto.response.UserResponseDto;

import java.util.List;

public interface UserService {
    UserResponseDto create(UserRequestDto requestDto);
    UserResponseDto update(UserUpdateDto user);
    UserResponseDto get();
    List<UserResponseDto> findAll(Integer page, Integer limit);
    void delete();
}