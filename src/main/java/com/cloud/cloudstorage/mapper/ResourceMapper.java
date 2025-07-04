package com.cloud.cloudstorage.mapper;

import com.cloud.cloudstorage.dto.DirectoryResponseDto;
import com.cloud.cloudstorage.dto.FileResponseDto;
import com.cloud.cloudstorage.dto.enums.ResourceType;
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
