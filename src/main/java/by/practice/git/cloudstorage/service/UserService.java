package by.practice.git.cloudstorage.service;

import by.practice.git.cloudstorage.dto.UserAuthDto;
import by.practice.git.cloudstorage.dto.UserCreateDto;
import by.practice.git.cloudstorage.dto.UserResponseDto;
import by.practice.git.cloudstorage.exception.EmailAlreadyExistException;
import by.practice.git.cloudstorage.exception.UserAlreadyExistException;
import by.practice.git.cloudstorage.model.Role;
import by.practice.git.cloudstorage.model.User;
import by.practice.git.cloudstorage.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class UserService implements IUserService {
    private final UserRepository userRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public UserService(UserRepository userRepository, RoleService roleService, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public UserResponseDto registerNewUserAccount(UserCreateDto userCreateDto, HttpServletRequest request) {
        String username = userCreateDto.getUsername();
        String password = userCreateDto.getPassword();

        if (userExists(username)) {
            throw new UserAlreadyExistException(username);
        }

        if (emailExists(userCreateDto.getEmail())) {
            throw new EmailAlreadyExistException(userCreateDto.getEmail());
        }

        User user = mapToUser(userCreateDto);
        userRepository.save(user);

        authenticate(username, password, request);

        return getUserResponseDto(username);
    }

    @Override
    public UserResponseDto authorizeUser(UserAuthDto userAuthDto, HttpServletRequest request) {
        String username = userAuthDto.getUsername();
        String password = userAuthDto.getPassword();

        authenticate(username, password, request);

        return getUserResponseDto(username);
    }

    @Override
    public UserResponseDto getUsersInfo(org.springframework.security.core.userdetails.User user) {
        return getUserResponseDto(user.getUsername());
    }

    private boolean userExists(String username) {
        return userRepository.existsUserByUsername(username);
    }

    private boolean emailExists(String email) {
        return userRepository.existsUserByEmail(email);
    }

    private UserResponseDto getUserResponseDto(String username) {
        return new UserResponseDto(username);
    }

    private User mapToUser(UserCreateDto userCreateDto) {
        User user = new User();
        user.setUsername(userCreateDto.getUsername());
        user.setPassword(passwordEncoder.encode(userCreateDto.getPassword()));
        user.setEmail(userCreateDto.getEmail());
        Role role = roleService.createRoleForNewUser();
        user.setRoles(List.of(role));
        return user;
    }

    private void authenticate(String username, String password, HttpServletRequest request) {
        SecurityContext securityContext = SecurityContextHolder.getContext();

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(
                        username,
                        password
                ));

        securityContext.setAuthentication(authentication);

        HttpSession session = request.getSession(true);
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, securityContext);
    }
}
