package by.practice.git.cloudstorage.mapper;

import by.practice.git.cloudstorage.dto.DirectoryResponseDto;
import by.practice.git.cloudstorage.dto.FileResponseDto;
import by.practice.git.cloudstorage.dto.enums.ResourceType;
import org.springframework.stereotype.Component;

@Component
public class ResourceMapper {

    public DirectoryResponseDto createDirectoryResponseDto(String parentPathForResponse, String directoryNameForResponse) {
        return new DirectoryResponseDto(
                parentPathForResponse,
                directoryNameForResponse,
                ResourceType.DIRECTORY
        );
    }

    public FileResponseDto createFileResponseDto(String parentPathForResponse, String filenameForResponse, Long size) {
        return new FileResponseDto(
                parentPathForResponse,
                filenameForResponse,
                size,
                ResourceType.FILE
        );
    }
}
