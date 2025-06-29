package by.practice.git.cloudstorage.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Schema(description = "DTO for uploading resources")
public class FileUploadDto {

    @Schema(
            description = "Uploading resource. Must be not empty",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotEmpty(message = "Uploading resource must not be empty")
    private List<MultipartFile> multipartFile;

    public FileUploadDto(List<MultipartFile> multipartFile) {
        this.multipartFile = multipartFile;
    }

    public FileUploadDto() {
    }

    public List<MultipartFile> getMultipartFile() {
        return multipartFile;
    }

    public void setMultipartFile(List<MultipartFile> multipartFile) {
        this.multipartFile = multipartFile;
    }
}
