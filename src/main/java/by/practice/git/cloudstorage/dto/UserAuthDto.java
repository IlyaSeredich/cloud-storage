package by.practice.git.cloudstorage.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "DTO for login")
public class UserAuthDto {
    @Schema(
            description = "Username of the user. Must be between 4 and 15 characters long and not blank.",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(message = "Username must not be blank")
    @Size(min = 4, max = 15, message = "Username must be between 4 and 15 characters long")
    private String username;
    @Schema(
            description = "Password of the user. Must have at least 6 characters and be not blank.",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(message = "Password must not be blank")
    @Size(min = 6, message = "Password must have at least 6 characters")
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
