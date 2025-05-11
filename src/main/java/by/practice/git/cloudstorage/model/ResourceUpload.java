package by.practice.git.cloudstorage.model;

import org.springframework.web.multipart.MultipartFile;

public class ResourceUpload {
    private MultipartFile multipartFile;

    public ResourceUpload(MultipartFile multipartFile) {
        this.multipartFile = multipartFile;
    }

    public ResourceUpload() {
    }

    public MultipartFile getMultipartFile() {
        return multipartFile;
    }

    public void setMultipartFile(MultipartFile multipartFile) {
        this.multipartFile = multipartFile;
    }


}
