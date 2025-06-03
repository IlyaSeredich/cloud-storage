package by.practice.git.cloudstorage.service.impl;

import by.practice.git.cloudstorage.dto.UserAuthDto;
import by.practice.git.cloudstorage.dto.UserCreateDto;
import by.practice.git.cloudstorage.dto.UserResponseDto;
import by.practice.git.cloudstorage.exception.EmailAlreadyExistException;
import by.practice.git.cloudstorage.exception.UserAlreadyExistException;
import by.practice.git.cloudstorage.mapper.UserMapper;
import by.practice.git.cloudstorage.model.User;
import by.practice.git.cloudstorage.repository.UserRepository;
import by.practice.git.cloudstorage.service.AuthenticationService;
import by.practice.git.cloudstorage.service.ResourceService;
import by.practice.git.cloudstorage.service.RoleService;
import by.practice.git.cloudstorage.service.UserAccountService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserAccountServiceImpl implements UserAccountService {
    private final UserRepository userRepository;
    private final RoleService roleService;
    private final AuthenticationService authenticationService;
    private final ResourceService resourceService;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserAccountServiceImpl(UserRepository userRepository, RoleService roleService, AuthenticationService authenticationService, ResourceService resourceService, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.authenticationService = authenticationService;
        this.resourceService = resourceService;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public UserResponseDto registerNewUser(UserCreateDto userCreateDto, HttpServletRequest request) {
        String username = userCreateDto.getUsername();
        String email = userCreateDto.getEmail();
        String password = userCreateDto.getPassword();

        validateRegistrationConditions(username, email);
        User user = createUser(userCreateDto);
        authenticate(username, password, request);
        resourceService.createRootDirectory(user.getId());
        return userMapper.toDto(username);
    }

    @Override
    public UserResponseDto authorizeUser(UserAuthDto userAuthDto, HttpServletRequest request) {
        String username = userAuthDto.getUsername();
        String password = userAuthDto.getPassword();

        authenticate(username, password, request);
        return userMapper.toDto(username);
    }

    private void validateRegistrationConditions(String username, String email) {
        validateUsernameNotExists(username);
        validateEmailNotExists(email);
    }

    private void authenticate(String username, String password, HttpServletRequest request) {
        authenticationService.authenticate(username, password, request);
    }

    private User createUser(UserCreateDto userCreateDto) {
        User user = userMapper.toUser(userCreateDto, roleService.getDefaultRole(), passwordEncoder);
        return userRepository.save(user);
    }

    private void validateUsernameNotExists(String username) {
        if (userExists(username)) {
            throw new UserAlreadyExistException(username);
        }
    }

    private void validateEmailNotExists(String email) {
        if (emailExists(email)) {
            throw new EmailAlreadyExistException(email);
        }
    }

    private boolean userExists(String username) {
        return userRepository.existsUserByUsername(username);
    }

    private boolean emailExists(String email) {
        return userRepository.existsUserByEmail(email);
    }
}
