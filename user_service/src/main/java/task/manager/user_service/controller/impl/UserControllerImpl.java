package task.manager.user_service.controller.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import task.manager.user_service.controller.UserController;
import task.manager.user_service.data.dto.request.UserRequestDto;
import task.manager.user_service.data.dto.request.UserUpdateDto;
import task.manager.user_service.data.dto.response.UserResponseDto;
import task.manager.user_service.service.UserService;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class UserControllerImpl implements UserController {

    private final UserService userService;

    @Override
    public ResponseEntity<UserResponseDto> create(UserRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.create(requestDto));
    }

    @Override
    public ResponseEntity<UserResponseDto> getById(UUID id) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getById(id));
    }

    @Override
    public ResponseEntity<UserResponseDto> updateById(UserUpdateDto updateDto, UUID id) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(userService.updateById(updateDto, id));
    }

    @Override
    public ResponseEntity<List<UserResponseDto>> getAll(Integer page, Integer limit) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.findAll(page, limit));
    }

    @Override
    public ResponseEntity<Void> deleteById(UUID id) {
        userService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}