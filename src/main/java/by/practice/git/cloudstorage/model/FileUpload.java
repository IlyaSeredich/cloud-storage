package by.practice.git.cloudstorage.model;

import org.springframework.web.multipart.MultipartFile;

public class FileUpload {
    private MultipartFile multipartFile;

    public FileUpload(MultipartFile multipartFile) {
        this.multipartFile = multipartFile;
    }

    public FileUpload() {
    }

    public MultipartFile getMultipartFile() {
        return multipartFile;
    }

    public void setMultipartFile(MultipartFile multipartFile) {
        this.multipartFile = multipartFile;
    }


}
