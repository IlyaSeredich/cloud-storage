package by.practice.git.cloudstorage.dto;

public class UserResponseDto {
    private final String username;

    public UserResponseDto(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

}
