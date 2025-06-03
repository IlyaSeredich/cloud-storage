package by.practice.git.cloudstorage.controller;

import by.practice.git.cloudstorage.dto.UserAuthDto;
import by.practice.git.cloudstorage.dto.UserCreateDto;
import by.practice.git.cloudstorage.exception.EmailAlreadyExistException;
import by.practice.git.cloudstorage.exception.UserAlreadyExistException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class UserControllerTest {
    private static final String USERNAME = "test-user";
    private static final String PASSWORD = "test-password";
    private static final String EMAIL = "test@gmail.com";
    private UserCreateDto userCreateDto;
    private UserAuthDto userAuthDto;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        userCreateDto = new UserCreateDto(USERNAME, PASSWORD, EMAIL);
        userAuthDto = new UserAuthDto(USERNAME, PASSWORD);
    }

    @Test
    void shouldRegisterNewUser() throws Exception {
        String url = "/api/auth/sign-up";

        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userCreateDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value(USERNAME))
                .andDo(print());
    }

    @Test
    void shouldRejectUserRegistrationWhenEmptyUsername() throws Exception {
        String invalidUsername = "  ";
        String url = "/api/auth/sign-up";
        UserCreateDto invalidUserCreateDto = new UserCreateDto(
                invalidUsername,
                PASSWORD,
                EMAIL
        );

        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidUserCreateDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").isNotEmpty())
                .andExpect(jsonPath("$.error").value(HttpStatus.BAD_REQUEST.getReasonPhrase()))
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.path").value(url))
                .andExpect(jsonPath("$.dateTime").isNotEmpty())
                .andDo(print());
    }

    @Test
    void shouldRejectUserRegistrationWhenUsernameTooShort() throws Exception {
        String invalidUsername = "abc";
        String url = "/api/auth/sign-up";
        UserCreateDto invalidUserCreateDto = new UserCreateDto(
                invalidUsername,
                PASSWORD,
                EMAIL
        );

        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidUserCreateDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").isNotEmpty())
                .andExpect(jsonPath("$.error").value(HttpStatus.BAD_REQUEST.getReasonPhrase()))
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.path").value(url))
                .andExpect(jsonPath("$.dateTime").isNotEmpty())
                .andDo(print());
    }

    @Test
    void shouldRejectUserRegistrationWhenUsernameTooLong() throws Exception {
        String invalidUsername = "a".repeat(16);
        String url = "/api/auth/sign-up";
        UserCreateDto invalidUserCreateDto = new UserCreateDto(
                invalidUsername,
                PASSWORD,
                EMAIL
        );

        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidUserCreateDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").isNotEmpty())
                .andExpect(jsonPath("$.error").value(HttpStatus.BAD_REQUEST.getReasonPhrase()))
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.path").value(url))
                .andExpect(jsonPath("$.dateTime").isNotEmpty())
                .andDo(print());
    }

    @Test
    void shouldRejectUserRegistrationWhenMissingUsername() throws Exception {
        String url = "/api/auth/sign-up";
        UserCreateDto invalidUserCreateDto = new UserCreateDto(
                null,
                PASSWORD,
                EMAIL
        );

        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidUserCreateDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").isNotEmpty())
                .andExpect(jsonPath("$.error").value(HttpStatus.BAD_REQUEST.getReasonPhrase()))
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.path").value(url))
                .andExpect(jsonPath("$.dateTime").isNotEmpty())
                .andDo(print());
    }

    @Test
    void shouldRejectUserRegistrationWhenEmptyPassword() throws Exception {
        String invalidPassword = "   ";
        String url = "/api/auth/sign-up";
        UserCreateDto invalidUserCreateDto = new UserCreateDto(
                USERNAME,
                invalidPassword,
                EMAIL
        );

        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidUserCreateDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").isNotEmpty())
                .andExpect(jsonPath("$.error").value(HttpStatus.BAD_REQUEST.getReasonPhrase()))
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.path").value(url))
                .andExpect(jsonPath("$.dateTime").isNotEmpty())
                .andDo(print());
    }

    @Test
    void shouldRejectUserRegistrationWhenMissingPassword() throws Exception {
        String url = "/api/auth/sign-up";
        UserCreateDto invalidUserCreateDto = new UserCreateDto(
                USERNAME,
                null,
                EMAIL
        );

        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidUserCreateDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").isNotEmpty())
                .andExpect(jsonPath("$.error").value(HttpStatus.BAD_REQUEST.getReasonPhrase()))
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.path").value(url))
                .andExpect(jsonPath("$.dateTime").isNotEmpty())
                .andDo(print());
    }

    @Test
    void shouldRejectUserRegistrationWhenEmptyEmail() throws Exception {
        String emptyEmail = "   ";
        String url = "/api/auth/sign-up";
        UserCreateDto invalidUserCreateDto = new UserCreateDto(
                USERNAME,
                PASSWORD,
                emptyEmail
        );

        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidUserCreateDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").isNotEmpty())
                .andExpect(jsonPath("$.error").value(HttpStatus.BAD_REQUEST.getReasonPhrase()))
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.path").value(url))
                .andExpect(jsonPath("$.dateTime").isNotEmpty())
                .andDo(print());
    }

    @Test
    void shouldRejectUserRegistrationWhenInvalidEmail() throws Exception {
        String invalidEmail = "invalidemail";
        String url = "/api/auth/sign-up";
        UserCreateDto invalidUserCreateDto = new UserCreateDto(
                USERNAME,
                PASSWORD,
                invalidEmail
        );

        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidUserCreateDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").isNotEmpty())
                .andExpect(jsonPath("$.error").value(HttpStatus.BAD_REQUEST.getReasonPhrase()))
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.path").value(url))
                .andExpect(jsonPath("$.dateTime").isNotEmpty())
                .andDo(print());
    }

    @Test
    void shouldRejectUserRegistrationWhenUsernameExists() throws Exception {
        String url = "/api/auth/sign-up";

        createUser();

        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userCreateDto)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value(UserAlreadyExistException.createErrorMessage(USERNAME)))
                .andExpect(jsonPath("$.error").value(HttpStatus.CONFLICT.getReasonPhrase()))
                .andExpect(jsonPath("$.status").value(HttpStatus.CONFLICT.value()))
                .andExpect(jsonPath("$.path").value(url))
                .andExpect(jsonPath("$.dateTime").isNotEmpty())
                .andDo(print());
    }

    @Test
    void shouldRejectUserRegistrationWhenEmailExists() throws Exception {
        String anotherUsername = "test-user2";
        String url = "/api/auth/sign-up";
        UserCreateDto secondUserCreateDto = new UserCreateDto(
                anotherUsername,
                PASSWORD,
                EMAIL
        );

        createUser();

        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(secondUserCreateDto)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value(EmailAlreadyExistException.createErrorMessage(EMAIL)))
                .andExpect(jsonPath("$.error").value(HttpStatus.CONFLICT.getReasonPhrase()))
                .andExpect(jsonPath("$.status").value(HttpStatus.CONFLICT.value()))
                .andExpect(jsonPath("$.path").value(url))
                .andExpect(jsonPath("$.dateTime").isNotEmpty())
                .andDo(print());
    }

    @Test
    void shouldAuthenticateUserAfterRegistration() throws Exception {
        String url = "/api/auth/sign-up";

        MockHttpSession mockHttpSession = new MockHttpSession();

        mockMvc.perform(post(url)
                        .session(mockHttpSession)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userCreateDto)))
                .andExpect(status().isCreated());


        SecurityContext securityContext =
                (SecurityContext) mockHttpSession.getAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY);

        assertThat(securityContext).isNotNull();
        Authentication authentication = securityContext.getAuthentication();
        assertThat(authentication).isNotNull();
        assertThat(authentication.isAuthenticated()).isTrue();
        assertThat(authentication.getName()).isEqualTo(USERNAME);
    }

    @Test
    void shouldAuthorizeExistingUser() throws Exception {
        String url = "/api/auth/sign-in";

        MockHttpSession mockHttpSession = new MockHttpSession();

        createUser();

        mockMvc.perform(post(url)
                        .session(mockHttpSession)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userAuthDto)))
                .andExpect(jsonPath("$.username").value(USERNAME))
                .andExpect(status().isOk())
                .andDo(print());

        SecurityContext securityContext =
                (SecurityContext) mockHttpSession.getAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY);
        assertThat(securityContext).isNotNull();
        Authentication authentication = securityContext.getAuthentication();
        assertThat(authentication).isNotNull();
        assertThat(authentication.isAuthenticated()).isTrue();
        assertThat(authentication.getName()).isEqualTo(USERNAME);
    }

    @Test
    void shouldRejectAuthorizingWhenBlankUsername() throws Exception {
        String url = "/api/auth/sign-in";
        String invalidUsername = "  ";
        UserAuthDto invalidUserAuthDto = new UserAuthDto(
                invalidUsername,
                PASSWORD
        );

        mockMvc.perform(post(url).
                        contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidUserAuthDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").isNotEmpty())
                .andExpect(jsonPath("$.error").value(HttpStatus.BAD_REQUEST.getReasonPhrase()))
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.path").value(url))
                .andExpect(jsonPath("$.dateTime").isNotEmpty())
                .andDo(print());
    }

    @Test
    void shouldRejectAuthorizingWhenTooShortUsername() throws Exception {
        String url = "/api/auth/sign-in";
        String invalidUsername = "abc";
        UserAuthDto invalidUserAuthDto = new UserAuthDto(
                invalidUsername,
                PASSWORD
        );

        mockMvc.perform(post(url).
                        contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidUserAuthDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").isNotEmpty())
                .andExpect(jsonPath("$.error").value(HttpStatus.BAD_REQUEST.getReasonPhrase()))
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.path").value(url))
                .andExpect(jsonPath("$.dateTime").isNotEmpty())
                .andDo(print());
    }

    @Test
    void shouldRejectAuthorizingWhenTooLongUsername() throws Exception {
        String url = "/api/auth/sign-in";
        String invalidUsername = "a".repeat(16);
        UserAuthDto invalidUserAuthDto = new UserAuthDto(
                invalidUsername,
                PASSWORD
        );

        mockMvc.perform(post(url).
                        contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidUserAuthDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").isNotEmpty())
                .andExpect(jsonPath("$.error").value(HttpStatus.BAD_REQUEST.getReasonPhrase()))
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.path").value(url))
                .andExpect(jsonPath("$.dateTime").isNotEmpty())
                .andDo(print());
    }

    @Test
    void shouldRejectAuthorizingWhenMissingUsername() throws Exception {
        String url = "/api/auth/sign-in";
        UserAuthDto invalidUserAuthDto = new UserAuthDto(
                null,
                PASSWORD
        );

        mockMvc.perform(post(url).
                        contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidUserAuthDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").isNotEmpty())
                .andExpect(jsonPath("$.error").value(HttpStatus.BAD_REQUEST.getReasonPhrase()))
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.path").value(url))
                .andExpect(jsonPath("$.dateTime").isNotEmpty())
                .andDo(print());
    }

    @Test
    void shouldRejectAuthorizingWhenEmptyPassword() throws Exception {
        String url = "/api/auth/sign-in";
        String invalidPassword = "   ";
        UserAuthDto invalidUserAuthDto = new UserAuthDto(
                USERNAME,
                invalidPassword
        );

        mockMvc.perform(post(url).
                        contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidUserAuthDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").isNotEmpty())
                .andExpect(jsonPath("$.error").value(HttpStatus.BAD_REQUEST.getReasonPhrase()))
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.path").value(url))
                .andExpect(jsonPath("$.dateTime").isNotEmpty())
                .andDo(print());
    }

    @Test
    void shouldRejectAuthorizingWhenMissingPassword() throws Exception {
        String url = "/api/auth/sign-in";
        UserAuthDto invalidUserAuthDto = new UserAuthDto(
                USERNAME,
                null
        );

        mockMvc.perform(post(url).
                        contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidUserAuthDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").isNotEmpty())
                .andExpect(jsonPath("$.error").value(HttpStatus.BAD_REQUEST.getReasonPhrase()))
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.path").value(url))
                .andExpect(jsonPath("$.dateTime").isNotEmpty())
                .andDo(print());
    }

    @Test
    void shouldRejectAuthorizingWhenUserNotExists() throws Exception {
        String url = "/api/auth/sign-in";
        UserAuthDto secondUserAuthDto = new UserAuthDto(
                "test-user2",
                "test-password2"
        );

        mockMvc.perform(post(url).
                        contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(secondUserAuthDto)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").isNotEmpty())
                .andExpect(jsonPath("$.error").value(HttpStatus.UNAUTHORIZED.getReasonPhrase()))
                .andExpect(jsonPath("$.status").value(HttpStatus.UNAUTHORIZED.value()))
                .andExpect(jsonPath("$.path").value(url))
                .andExpect(jsonPath("$.dateTime").isNotEmpty())
                .andDo(print());
    }

    @Test
    void shouldGetUsersInfoAfterRegistration() throws Exception {
        String url = "/api/user/me";
        MockHttpSession mockHttpSession = new MockHttpSession();

        mockMvc.perform(post("/api/auth/sign-up")
                        .session(mockHttpSession)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userCreateDto)))
                .andExpect(status().isCreated());

        mockMvc.perform(get(url)
                        .session(mockHttpSession))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(USERNAME))
                .andDo(print());
    }

    @Test
    void shouldGetUsersInfoAfterAuthorization() throws Exception {
        String url = "/api/user/me";
        MockHttpSession mockHttpSession = new MockHttpSession();

        createUser();

        mockMvc.perform(post("/api/auth/sign-in")
                        .session(mockHttpSession)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userAuthDto)))
                .andExpect(status().isOk());


        mockMvc.perform(get(url)
                        .session(mockHttpSession))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(USERNAME))
                .andDo(print());
    }

    @Test
    void shouldRejectGettingUsersInfoWhenUserNotAuthorized() throws Exception {
        String url = "/api/user/me";

        mockMvc.perform(get(url))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").isNotEmpty())
                .andExpect(jsonPath("$.error").value(HttpStatus.UNAUTHORIZED.getReasonPhrase()))
                .andExpect(jsonPath("$.status").value(HttpStatus.UNAUTHORIZED.value()))
                .andExpect(jsonPath("$.path").value(url))
                .andExpect(jsonPath("$.dateTime").isNotEmpty())
                .andDo(print());
    }


    private void createUser() throws Exception {
        String url = "/api/auth/sign-up";

        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userCreateDto)))
                .andExpect(status().isCreated());
    }


}