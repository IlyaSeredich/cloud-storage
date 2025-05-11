package by.practice.git.cloudstorage.dto;

public class CreateDirectoryResponseDto {
    public CreateDirectoryResponseDto(String path, String name, ResourceType type) {
        this.path = path;
        this.name = name;
        this.type = type;
    }

    private String path;
    private String name;
    private ResourceType type;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ResourceType getType() {
        return type;
    }

    public void setResourceType(ResourceType type) {
        this.type = type;
    }
}
