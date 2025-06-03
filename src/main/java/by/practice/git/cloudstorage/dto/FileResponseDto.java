package by.practice.git.cloudstorage.dto;

import by.practice.git.cloudstorage.dto.enums.ResourceType;

public class FileResponseDto extends BaseResourceResponseDto {
    private Long size;

    public FileResponseDto(String path, String name, Long size, ResourceType type) {
        super(path, name, type);
        this.size = size;
    }

    public Long getSize() {
        return size;
    }
}
