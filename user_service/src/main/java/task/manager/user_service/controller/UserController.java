package task.manager.user_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import task.manager.user_service.data.dto.request.UserRequestDto;
import task.manager.user_service.data.dto.request.UserUpdateDto;
import task.manager.user_service.data.dto.response.UserResponseDto;

import java.util.List;
import java.util.UUID;

@RequestMapping("/api/v1/users")
public interface UserController {

    @PostMapping
    ResponseEntity<UserResponseDto> create(@RequestBody UserRequestDto requestDto);

    @GetMapping("/{id}")
    ResponseEntity<UserResponseDto> getById(@PathVariable UUID id);

    @PatchMapping("/{id}")
    ResponseEntity<UserResponseDto> updateById(@RequestBody UserUpdateDto updateDto, @PathVariable UUID id);

    @GetMapping
    ResponseEntity<List<UserResponseDto>> getAll(@RequestParam Integer page, @RequestParam Integer limit);

    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteById(@PathVariable UUID id);
}