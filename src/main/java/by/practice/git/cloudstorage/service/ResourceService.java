package by.practice.git.cloudstorage.service;

import by.practice.git.cloudstorage.dto.*;
import by.practice.git.cloudstorage.dto.FileResponseDtoDto;
import by.practice.git.cloudstorage.dto.BaseResourceResponseDto;
import org.springframework.security.core.userdetails.User;

import java.util.List;

public interface ResourceService {
    void createRootDirectory(Long userId);
    List<FileResponseDtoDto> uploadFiles(String path, FileUploadDto fileUploadDto, User user);
    DirectoryResponseDto createEmptyDirectory(String path, User user);
    List<BaseResourceResponseDto> getDirectoryContent(String directoryPath, User user);
}
