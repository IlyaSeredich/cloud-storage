package by.practice.git.cloudstorage.service;

import by.practice.git.cloudstorage.dto.*;
import by.practice.git.cloudstorage.dto.FileResponseDto;
import by.practice.git.cloudstorage.dto.BaseResourceResponseDto;
import jakarta.validation.constraints.NotBlank;
import org.springframework.security.core.userdetails.User;

import java.util.List;

public interface ResourceService {
    void createRootDirectory(Long userId);
    List<FileResponseDto> uploadFiles(String path, FileUploadDto fileUploadDto, User user);
    BaseResourceResponseDto createEmptyDirectory(String path, User user);
    List<BaseResourceResponseDto> getDirectoryContent(String directoryPath, User user);
    List<BaseResourceResponseDto> getSearchedContent(String query, User user);
    BaseResourceResponseDto moveResource(String from, String to, User user);
}
