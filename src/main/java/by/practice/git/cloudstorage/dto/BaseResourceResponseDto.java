package by.practice.git.cloudstorage.dto;

import by.practice.git.cloudstorage.dto.enums.ResourceType;

public abstract class BaseResourceResponseDto {
    private final String path;
    private final String name;
    private final ResourceType type;

    public BaseResourceResponseDto(String path, String name, ResourceType type) {
        this.path = path;
        this.name = name;
        this.type = type;
    }

    public String getPath() {
        return path;
    }

    public String getName() {
        return name;
    }

    public ResourceType getType() {
        return type;
    }
}
