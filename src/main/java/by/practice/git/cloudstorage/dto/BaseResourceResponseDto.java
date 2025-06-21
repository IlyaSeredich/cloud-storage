package by.practice.git.cloudstorage.dto;

import by.practice.git.cloudstorage.dto.enums.ResourceType;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Abstract class for DirectoryResponseDto and FileResponseDto")
public abstract class BaseResourceResponseDto {
    @Schema(description = "Parent path of the resource")
    private final String path;
    @Schema(description = "Name of the resource")
    private final String name;
    @Schema(description = "Type of the resource")
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
