package task.manager.user_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import task.manager.user_service.data.dto.request.UserRequestDto;
import task.manager.user_service.data.dto.request.UserUpdateDto;
import task.manager.user_service.data.dto.response.UserResponseDto;

import java.util.List;

@RequestMapping("/api/v1/users")
public interface UserController {

    @PostMapping
    ResponseEntity<UserResponseDto> create(@RequestBody UserRequestDto requestDto);

    @GetMapping
    ResponseEntity<UserResponseDto> get();

    @PatchMapping
    ResponseEntity<UserResponseDto> update(@RequestBody UserUpdateDto updateDto);

    @GetMapping("/all")
    ResponseEntity<List<UserResponseDto>> getAll(@RequestParam Integer page, @RequestParam Integer limit);

    @DeleteMapping
    ResponseEntity<Void> delete();
}