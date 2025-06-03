package by.practice.git.cloudstorage.service.impl;

import by.practice.git.cloudstorage.dto.UserResponseDto;
import by.practice.git.cloudstorage.mapper.UserMapper;
import by.practice.git.cloudstorage.model.User;
import by.practice.git.cloudstorage.repository.UserRepository;
import by.practice.git.cloudstorage.service.CurrentUserService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CurrentUserServiceImpl implements CurrentUserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public CurrentUserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public UserResponseDto getCurrentUserInfo(org.springframework.security.core.userdetails.User user) {
        String username = user.getUsername();
        return userMapper.toDto(username);
    }

    @Override
    public Long getCurrentUserId(org.springframework.security.core.userdetails.User user) {
        String username = user.getUsername();
        User foundUser = getUserByUsername(username);

        return foundUser.getId();
    }

    private User getUserByUsername(String username) {
        return userRepository.findUserByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException(String.format("Username %s not found", username)));
    }


}
