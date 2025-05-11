package by.practice.git.cloudstorage.service;

import by.practice.git.cloudstorage.dto.CreateDirectoryResponseDto;
import by.practice.git.cloudstorage.dto.FileUploadDto;
import org.springframework.security.core.userdetails.User;

public interface IResourceService {
    void createRootDir(Long userId);
    String upload(FileUploadDto fileUploadDto);
    CreateDirectoryResponseDto createEmptyDir(User user, String path);
}
