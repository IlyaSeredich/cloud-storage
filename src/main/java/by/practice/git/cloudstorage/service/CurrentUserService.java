package by.practice.git.cloudstorage.service;

import by.practice.git.cloudstorage.dto.UserResponseDto;
import org.springframework.security.core.userdetails.User;

public interface CurrentUserService {
    UserResponseDto getCurrentUserInfo(User user);
    Long getCurrentUserId(User user);
}