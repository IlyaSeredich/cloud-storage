package by.practice.git.cloudstorage.dto;

public class UserResponseDto {
    private String username;

    public UserResponseDto(String username) {
        this.username = username;
    }

    public UserResponseDto() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
