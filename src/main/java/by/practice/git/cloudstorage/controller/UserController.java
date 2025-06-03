package by.practice.git.cloudstorage.controller;

import by.practice.git.cloudstorage.dto.UserAuthDto;
import by.practice.git.cloudstorage.dto.UserCreateDto;
import by.practice.git.cloudstorage.dto.UserResponseDto;
import by.practice.git.cloudstorage.service.UserAccountService;
import by.practice.git.cloudstorage.service.CurrentUserService;
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
    private final CurrentUserService currentUserService;
    private final UserAccountService userAccountService;

    public UserController(CurrentUserService currentUserService, UserAccountService userAccountService) {
        this.currentUserService = currentUserService;
        this.userAccountService = userAccountService;
    }

    @PostMapping("/auth/sign-up")
    public ResponseEntity<UserResponseDto> register(@Valid @RequestBody UserCreateDto userCreateDto,
                                                    HttpServletRequest request) {
        UserResponseDto userResponseDto = userAccountService.registerNewUser(userCreateDto, request);
        return new ResponseEntity<>(userResponseDto, HttpStatus.CREATED);
    }

    @PostMapping("/auth/sign-in")
    public ResponseEntity<UserResponseDto> authorize(
            @Valid @RequestBody UserAuthDto userAuthDto,
            HttpServletRequest request) {
        UserResponseDto userResponseDto = userAccountService.authorizeUser(userAuthDto, request);
        return new ResponseEntity<>(userResponseDto, HttpStatus.OK);
    }

    @GetMapping("/user/me")
    public ResponseEntity<UserResponseDto> me(@AuthenticationPrincipal User user) {
        UserResponseDto userResponseDto = currentUserService.getCurrentUserInfo(user);
        return new ResponseEntity<>(userResponseDto, HttpStatus.OK);
    }
}
