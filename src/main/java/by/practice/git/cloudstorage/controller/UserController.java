package by.practice.git.cloudstorage.controller;

import by.practice.git.cloudstorage.dto.UserAuthDto;
import by.practice.git.cloudstorage.dto.UserCreateDto;
import by.practice.git.cloudstorage.dto.UserResponseDto;
import by.practice.git.cloudstorage.service.impl.UserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController {
    private final UserServiceImpl userServiceImpl;

    public UserController(UserServiceImpl userServiceImpl) {
        this.userServiceImpl = userServiceImpl;
    }

    @PostMapping("/auth/sign-up")
    public ResponseEntity<UserResponseDto> register(@Valid @RequestBody UserCreateDto userCreateDto,
                                                    HttpServletRequest request) {
        UserResponseDto userResponseDto = userServiceImpl.registerNewUserAccount(userCreateDto, request);

        return new ResponseEntity<>(userResponseDto, HttpStatus.CREATED);
    }

    @PostMapping("/auth/sign-in")
    public ResponseEntity<UserResponseDto> authorize(
            @Valid @RequestBody UserAuthDto userAuthDto,
            HttpServletRequest request) {
        UserResponseDto userResponseDto = userServiceImpl.authorizeUser(userAuthDto, request);

        return new ResponseEntity<>(userResponseDto, HttpStatus.OK);
    }

    @GetMapping("/user/me")
    public ResponseEntity<UserResponseDto> me(@AuthenticationPrincipal User user) {
        UserResponseDto userResponseDto = userServiceImpl.getUsersInfo(user);
        return new ResponseEntity<>(userResponseDto, HttpStatus.OK);
    }
}
