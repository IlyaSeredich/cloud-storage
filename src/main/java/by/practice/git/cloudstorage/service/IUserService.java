package by.practice.git.cloudstorage.service;

import by.practice.git.cloudstorage.dto.UserAuthDto;
import by.practice.git.cloudstorage.dto.UserCreateDto;
import by.practice.git.cloudstorage.dto.UserResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.userdetails.User;

public interface IUserService {
    UserResponseDto registerNewUserAccount(UserCreateDto userCreateDto, HttpServletRequest request);
    UserResponseDto authorizeUser(UserAuthDto userAuthDto, HttpServletRequest request);
    UserResponseDto getUsersInfo(User user);
    Long getUsersId(User user);
}