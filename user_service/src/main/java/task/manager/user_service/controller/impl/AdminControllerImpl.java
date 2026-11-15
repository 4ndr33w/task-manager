package task.manager.user_service.controller.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import task.manager.user_service.controller.AdminController;
import task.manager.user_service.data.dto.request.UserUpdateDto;
import task.manager.user_service.data.dto.response.UserResponseDto;
import task.manager.user_service.service.AdminService;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class AdminControllerImpl implements AdminController {

    private final AdminService adminService;

    @Override
    public ResponseEntity<UserResponseDto> updateById(UserUpdateDto updateDto, UUID id) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(adminService.updateById(updateDto, id));
    }

    @Override
    public ResponseEntity<Void> deleteById(UUID id) {
        adminService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Override
    public ResponseEntity<UserResponseDto> getById(UUID id) {
        return ResponseEntity.status(HttpStatus.OK).body(adminService.getById(id));
    }
}