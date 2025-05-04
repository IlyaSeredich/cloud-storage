package by.practice.git.cloudstorage.dto;

import org.springframework.web.multipart.MultipartFile;

public class FileUploadDto {
    private MultipartFile multipartFile;

    public FileUploadDto(MultipartFile multipartFile) {
        this.multipartFile = multipartFile;
    }

    public FileUploadDto() {
    }

    public MultipartFile getMultipartFile() {
        return multipartFile;
    }

    public void setMultipartFile(MultipartFile multipartFile) {
        this.multipartFile = multipartFile;
    }
}
