package by.practice.git.cloudstorage.mapper;

import by.practice.git.cloudstorage.dto.UserCreateDto;
import by.practice.git.cloudstorage.dto.UserResponseDto;
import by.practice.git.cloudstorage.model.Role;
import by.practice.git.cloudstorage.model.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserMapperDepr {
    private final PasswordEncoder passwordEncoder;

    public UserMapperDepr(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponseDto toDto(String username) {
        return new UserResponseDto(username);
    }

    public User toUser(UserCreateDto userCreateDto, Role defaultRole) {
        User user = new User();
        user.setUsername(userCreateDto.getUsername());
        user.setPassword(passwordEncoder.encode(userCreateDto.getPassword()));
        user.setEmail(userCreateDto.getEmail());
        user.setRoles(List.of(defaultRole));
        return user;
    }
}
