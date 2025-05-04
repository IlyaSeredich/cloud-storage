package by.practice.git.cloudstorage.service;

import by.practice.git.cloudstorage.dto.FileUploadDto;
import org.springframework.security.core.userdetails.User;

public interface IFileService {
    String upload(User user, FileUploadDto fileUploadDto);
}
