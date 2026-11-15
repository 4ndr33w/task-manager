package task.manager.user_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import task.manager.user_service.data.dto.request.UserUpdateDto;
import task.manager.user_service.data.dto.response.UserResponseDto;

import java.util.UUID;

@RequestMapping("/api/v1/admin")
public interface AdminController {

    @PatchMapping("/{id}")
    ResponseEntity<UserResponseDto> updateById(@RequestBody UserUpdateDto updateDto, @PathVariable UUID id);

    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteById(@PathVariable UUID id);

    @GetMapping("/{id}")
    ResponseEntity<UserResponseDto> getById(@PathVariable UUID id);
}