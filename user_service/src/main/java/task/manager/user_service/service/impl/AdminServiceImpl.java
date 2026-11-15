package task.manager.user_service.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import task.manager.user_service.data.dto.request.UserUpdateDto;
import task.manager.user_service.data.dto.response.UserResponseDto;
import task.manager.user_service.data.entity.User;
import task.manager.user_service.data.mapper.UserMapper;
import task.manager.user_service.data.repository.UserRepository;
import task.manager.user_service.exception.UserNotFoundException;
import task.manager.user_service.service.AdminService;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public UserResponseDto updateById(UserUpdateDto updateDto, UUID id) {
        User existingUser = getUserByIdOrElseThrow(id);
        User updatedUser = userMapper.toUpdatedEntity(updateDto, existingUser);
        log.info("Обновлен профиль пользователя с id: %s".formatted(id));

        return userMapper.toResponse(updatedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDto getById(UUID id) {
        User existingUser = getUserByIdOrElseThrow(id);
        return userMapper.toResponse(existingUser);
    }

    @Override
    public void deleteById(UUID id) {
        userRepository.deleteById(id);
        log.info("Удалены данные профиля пользователя с id: {}", id);
    }

    private User getUserByIdOrElseThrow(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Не найден пользователь с id: %s".formatted(id)));
    }
}