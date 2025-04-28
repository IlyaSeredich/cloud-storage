package by.practice.git.cloudstorage.dto;

import by.practice.git.cloudstorage.validation.ValidEmail;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


public class UserCreateDto {
    @NotBlank
    @Size(min = 4, max = 15)
    private String username;

    @NotBlank
    private String password;

    @NotBlank
    @ValidEmail
    private String email;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
