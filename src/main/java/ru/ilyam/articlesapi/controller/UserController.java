package ru.ilyam.articlesapi.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.ilyam.articlesapi.dto.RegisterRequestDto;
import ru.ilyam.articlesapi.dto.UserResponseDto;
import ru.ilyam.articlesapi.service.UserService;

@RestController
@RequestMapping("/api/v1/user-management")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/users")
    public ResponseEntity<Page<UserResponseDto>> getAll(
            @RequestParam(value = "pageNumber", defaultValue = "0") @Min(0) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10") @Min(1) Integer pageSize
    ) {
        return new ResponseEntity<>(userService.getAll(PageRequest.of(pageNumber, pageSize)), HttpStatus.OK);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<UserResponseDto> getUser(@PathVariable("id") Long id) {
        return new ResponseEntity<>(userService.findById(id), HttpStatus.OK);
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable("id") Long id) {
        userService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/user/{id}")
    public ResponseEntity<UserResponseDto> update(
            @PathVariable("id") Long id,
            @Valid @RequestBody RegisterRequestDto registerRequestDto
    ) {
        return new ResponseEntity<>(userService.update(id, registerRequestDto), HttpStatus.OK);
    }


}
