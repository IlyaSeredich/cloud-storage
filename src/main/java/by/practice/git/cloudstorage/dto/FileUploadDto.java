package by.practice.git.cloudstorage.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


public class FileUploadDto {

    @NotEmpty(message = "Uploading resource should not be empty")
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
