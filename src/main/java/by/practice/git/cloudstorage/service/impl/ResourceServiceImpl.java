package by.practice.git.cloudstorage.service.impl;

import by.practice.git.cloudstorage.config.MinioProperties;
import by.practice.git.cloudstorage.dto.CreateDirectoryResponseDto;
import by.practice.git.cloudstorage.dto.FileUploadDto;
import by.practice.git.cloudstorage.dto.ResourceType;
import by.practice.git.cloudstorage.exception.CreateRootMinioDirectoryException;
import by.practice.git.cloudstorage.exception.MinioCreatingDirectoryException;
import by.practice.git.cloudstorage.exception.MinioDirectoryAlreadyExistsException;
import by.practice.git.cloudstorage.exception.MinioExistingParentDirectoryException;
import by.practice.git.cloudstorage.service.IResourceService;
import by.practice.git.cloudstorage.service.IUserService;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.StatObjectArgs;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

@Service
public class ResourceServiceImpl implements IResourceService {
    private final MinioClient minioClient;
    private final MinioProperties minioProperties;
    private final IUserService userService;

    public ResourceServiceImpl(MinioClient minioClient, MinioProperties minioProperties, IUserService userService) {
        this.minioClient = minioClient;
        this.minioProperties = minioProperties;
        this.userService = userService;
    }

    @Override
    public void createRootDir(Long userId) {
        String dirName = String.format("user-%d-files/", userId);
        try (InputStream inputStream = new ByteArrayInputStream(new byte[0])) {
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(minioProperties.getBucket())
                    .object(dirName)
                    .stream(inputStream, 0, -1)
                    .contentType("application/x-directory")
                    .build());
        } catch (Exception ex) {
            throw new CreateRootMinioDirectoryException();
        }
    }

    @Override
    public String upload(FileUploadDto fileUploadDto) {

        return "";
    }

    @Override
    public CreateDirectoryResponseDto createEmptyDir(User user, String path) {
        Long userId = userService.getUsersId(user);
        String rootDir = String.format("user-%d-files/", userId);
        String normalizedPath = normalizePath(path);
        String fullPath = rootDir + normalizedPath;
        String parentDir = extractParentDirPath(fullPath);

        parentDirectoryExists(parentDir);
        boolean creatingDirectoryExists = directoryExists(fullPath);

        if(creatingDirectoryExists) {
            throw new MinioDirectoryAlreadyExistsException(normalizedPath);
        }

        try(InputStream inputStream = new ByteArrayInputStream(new byte[0])) {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(minioProperties.getBucket())
                            .object(fullPath)
                            .stream(inputStream, 0, -1)
                            .contentType("application/x-directory")
                            .build()
            );
        } catch (Exception ex) {
            throw new MinioCreatingDirectoryException(normalizedPath);
        }

        String responsePath = formatResponsePath(normalizedPath);
        String responseDirName = formatDirResponseName(responsePath, normalizedPath);
        return createDirectoryResponse(responsePath, responseDirName);
    }

    private String extractParentDirPath(String fullPath) {
        int index = fullPath.lastIndexOf("/", fullPath.length() - 2);
        return fullPath.substring(0, index + 1);
    }

    private String normalizePath(String path) {
        String normalized = path.startsWith("/") ? path.substring(1) : path;

        if (!normalized.endsWith("/")) {
            normalized += "/";
        }

        return normalized;
    }

    private void parentDirectoryExists(String parentPath) {
        try {
            minioClient.statObject(
                    StatObjectArgs.builder()
                            .bucket(minioProperties.getBucket())
                            .object(parentPath)
                            .build());
        } catch (Exception ex) {
            int index = parentPath.indexOf("/");
            String responsePath = parentPath.substring(index + 1);
            throw new MinioExistingParentDirectoryException(responsePath);
        }
    }

    private boolean directoryExists(String path) {
        try{
            minioClient.statObject(
                    StatObjectArgs.builder()
                            .bucket(minioProperties.getBucket())
                            .object(path)
                            .build()
            );
        } catch (Exception ex) {
            return false;
        }

        return true;
    }

    private String formatResponsePath(String normalizedPath) {
        int index = normalizedPath.lastIndexOf("/", normalizedPath.length() - 2);
        return normalizedPath.substring(0, index + 1);
    }

    private String formatDirResponseName(String extractPath, String normalizedPath) {
        return normalizedPath.substring(extractPath.length(), normalizedPath.length() -1);
    }

    private CreateDirectoryResponseDto createDirectoryResponse(String path, String name) {
        return new CreateDirectoryResponseDto(
                path,
                name,
                ResourceType.DIRECTORY
        );
    }


}
