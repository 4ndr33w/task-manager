package task.manager.user_service.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import task.manager.user_service.data.dto.request.UserRequestDto;
import task.manager.user_service.data.dto.request.UserUpdateDto;
import task.manager.user_service.data.dto.response.UserResponseDto;
import task.manager.user_service.data.entity.User;
import task.manager.user_service.data.mapper.UserMapper;
import task.manager.user_service.data.repository.UserRepository;
import task.manager.user_service.exception.AuthorizationException;
import task.manager.user_service.exception.UserNotFoundException;
import task.manager.user_service.security.details.AppUserDetails;
import task.manager.user_service.service.UserService;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public UserResponseDto create(UserRequestDto requestDto) {
        User savedUser = userRepository.save(userMapper.toEntity(requestDto));
        log.info("Создан пользователь с id: {}, email: {}", savedUser.getId(), savedUser.getEmail());

        return userMapper.toResponse(savedUser);
    }

    @Override
    @Transactional
    public UserResponseDto update(UserUpdateDto updateDto) {
        User existingUser = getUserByIdOrElseThrow(getUserIdFromSecurityContext());
        User updatedUser = userMapper.toUpdatedEntity(updateDto, existingUser);
        log.info("Обновлен профиль пользователя с id: %s".formatted(getUserIdFromSecurityContext()));

        return userMapper.toResponse(updatedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDto get() {
        User existingUser = getUserByIdOrElseThrow(getUserIdFromSecurityContext());
        return userMapper.toResponse(existingUser);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDto> findAll(Integer page, Integer limit) {
        Pageable pageable = setPageable(page, limit);
        Page<User> users = userRepository.findAll(pageable);

        return users.stream()
                .map(userMapper::toResponse)
                .toList();
    }

    @Override
    public void delete() {
        userRepository.deleteById(getUserIdFromSecurityContext());
        log.info("Удалены данные профиля пользователя с id: {}", getUserIdFromSecurityContext());
    }

    private User getUserByIdOrElseThrow(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Не найден пользователь с id: %s".formatted(id)));
    }

    private Pageable setPageable(Integer page, Integer limit) {
        int paginationPage = (page == null || page < 1) ? 0 : page - 1;
        int pageLimit = setPageLimit(limit);

        return PageRequest.of(paginationPage, pageLimit);
    }

    private int setPageLimit(Integer limit) {
        if (limit == null || limit < 1) {
            return 5;
        }
        if (limit > 20) {
            return 20;
        }
        return limit;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User existingUser = userRepository.findByEmail(username)
                .orElseThrow(() -> new UserNotFoundException("Не найден пользователь с email: %s".formatted(username)));
        return new AppUserDetails(existingUser);
    }

    private UUID getUserIdFromSecurityContext() {
        try {
            return (UUID) Objects.requireNonNull(SecurityContextHolder
                    .getContext()
                    .getAuthentication())
                    .getCredentials();
        }
        catch (Exception e) {
            log.error("Ошибка при извлечении пользовательских данных из SecurityContextHolder: {}", e.getMessage(), e);
            throw new AuthorizationException("Ошибка при извлечении пользовательских данных из SecurityContextHolder");
        }
    }
}