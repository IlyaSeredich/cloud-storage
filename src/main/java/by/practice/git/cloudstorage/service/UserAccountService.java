package by.practice.git.cloudstorage.service;

import by.practice.git.cloudstorage.dto.UserAuthDto;
import by.practice.git.cloudstorage.dto.UserCreateDto;
import by.practice.git.cloudstorage.dto.UserResponseDto;
import jakarta.servlet.http.HttpServletRequest;

public interface UserAccountService {
    UserResponseDto registerNewUser(UserCreateDto userCreateDto, HttpServletRequest request);
    UserResponseDto authorizeUser(UserAuthDto userAuthDto, HttpServletRequest request);
}