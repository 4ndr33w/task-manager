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

@RestController
@RequiredArgsConstructor
public class UserControllerImpl implements UserController {

    private final UserService userService;

    @Override
    public ResponseEntity<UserResponseDto> create(UserRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.create(requestDto));
    }

    @Override
    public ResponseEntity<UserResponseDto> get() {
        return ResponseEntity.status(HttpStatus.OK).body(userService.get());
    }

    @Override
    public ResponseEntity<UserResponseDto> update(UserUpdateDto updateDto) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(userService.update(updateDto));
    }

    @Override
    public ResponseEntity<List<UserResponseDto>> getAll(Integer page, Integer limit) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.findAll(page, limit));
    }

    @Override
    public ResponseEntity<Void> delete() {
        userService.delete();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}