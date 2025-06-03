package by.practice.git.cloudstorage.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


public class UserAuthDto {
    @NotBlank
    @Size(min = 4, max = 15)
    private String username;
    @NotBlank
    private String password;

    public UserAuthDto(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public UserAuthDto() {
    }

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
}
