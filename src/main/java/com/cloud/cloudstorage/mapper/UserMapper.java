package com.cloud.cloudstorage.mapper;

import com.cloud.cloudstorage.dto.UserCreateDto;
import com.cloud.cloudstorage.dto.UserResponseDto;
import com.cloud.cloudstorage.model.Role;
import com.cloud.cloudstorage.model.User;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserMapper {
    public UserResponseDto toDto(String username) {
        return new UserResponseDto(username);
    }

    public User toUser(UserCreateDto userCreateDto, Role defaultRole, PasswordEncoder passwordEncoder) {
        User user = new User();
        user.setUsername(userCreateDto.getUsername());
        user.setPassword(passwordEncoder.encode(userCreateDto.getPassword()));
        user.setEmail(userCreateDto.getEmail());
        user.setRoles(List.of(defaultRole));
        return user;
    }
}
