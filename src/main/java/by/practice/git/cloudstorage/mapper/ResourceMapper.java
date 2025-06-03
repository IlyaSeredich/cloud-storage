package by.practice.git.cloudstorage.mapper;

import by.practice.git.cloudstorage.dto.DirectoryResponseDto;
import by.practice.git.cloudstorage.service.PathFormatterService;
import org.springframework.stereotype.Component;

@Component
public class ResourceMapper {

    public DirectoryResponseDto toDto(String fullPath, PathFormatterService pathAdapterService) {
        return null;
    }
}
