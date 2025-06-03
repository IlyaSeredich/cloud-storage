package by.practice.git.cloudstorage.dto;

import by.practice.git.cloudstorage.dto.enums.ResourceType;

public class DirectoryResponseDto extends BaseResourceResponseDto {
    public DirectoryResponseDto(String path, String name, ResourceType type) {
        super(path, name, type);
    }

}
