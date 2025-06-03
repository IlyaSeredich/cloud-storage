package by.practice.git.cloudstorage.service.impl;

import by.practice.git.cloudstorage.dto.*;
import by.practice.git.cloudstorage.dto.DirectoryResponseDto;
import by.practice.git.cloudstorage.dto.BaseResourceResponseDto;
import by.practice.git.cloudstorage.dto.enums.ResourceType;
import by.practice.git.cloudstorage.exception.MinioDirectoryAlreadyExistsException;
import by.practice.git.cloudstorage.exception.MinioDirectoryNotExistsException;
import by.practice.git.cloudstorage.exception.MinioExistingParentDirectoryException;
import by.practice.git.cloudstorage.exception.MinioUploadingResourceAlreadyExistsException;
import by.practice.git.cloudstorage.service.*;
import io.minio.messages.Item;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
public class MinioResourceServiceImpl implements ResourceService {
    private final CurrentUserService currentUserService;
    private final PathBuilderService pathBuilderService;
    private final PathFormatterService pathFormatterService;
    private final MinioStorageService minioStorageService;

    public MinioResourceServiceImpl(CurrentUserService currentUserService, PathBuilderService pathBuilderService, PathFormatterService pathFormatterService, MinioStorageService minioStorageService) {
        this.currentUserService = currentUserService;
        this.pathBuilderService = pathBuilderService;
        this.pathFormatterService = pathFormatterService;
        this.minioStorageService = minioStorageService;
    }

    @Override
    public void createRootDirectory(Long userId) {
        String rootDirName = getRootDirName(userId);
        putRootDirectory(rootDirName);
    }

    @Override
    public DirectoryResponseDto createEmptyDirectory(String directoryPathFromRequest, User user) {
        String fullPath = getDirectoryFullPath(user, directoryPathFromRequest);
        validateCreatingDirectoryConditions(fullPath);
        putEmptyDirectoryInMinio(fullPath);
        return createDirectoryResponseDto(fullPath);
    }

    @Override
    public List<FileResponseDtoDto> uploadFiles(String parentPathFromRequest, FileUploadDto fileUploadDto, User user) {
        List<MultipartFile> multipartFileList = fileUploadDto.getMultipartFile();
        String parentPath = getDirectoryFullPath(user, parentPathFromRequest);
        validateFileParentDirectoryExists(parentPath);
        return uploadValidatedFiles(parentPathFromRequest, multipartFileList, user);
    }

    @Override
    public List<BaseResourceResponseDto> getDirectoryContent(String directoryPathFromRequest, User user) {
        String fullPath = getDirectoryFullPath(user, directoryPathFromRequest);
        validateDirectoryExists(fullPath);
        return createResourceResponseDtoList(getMinioDirectoryContentList(fullPath), directoryPathFromRequest);
    }

    private List<BaseResourceResponseDto> createResourceResponseDtoList(List<Item> directoryObjectsList, String directoryPathFromRequest) {
        List<BaseResourceResponseDto> dtoList = new ArrayList<>();
        directoryObjectsList.forEach(item -> {
            String itemName = item.objectName();
            if(item.isDir()) {
                dtoList.add(createDirectoryResponseDto(directoryPathFromRequest, getDirectoryNameForResponse(itemName)));
            } else {
                dtoList.add(createFileResponseDto(directoryPathFromRequest, getFilenameForResponse(itemName), item.size()));
            }
        });
        return dtoList;
    }

    private String getRootDirName(Long userId) {
        return pathBuilderService.createRootDirName(userId);
    }

    private void putRootDirectory(String rootDirName) {
        minioStorageService.putRootDirectory(rootDirName);
    }

    private List<Item> getMinioDirectoryContentList(String fullPath) {
        return minioStorageService.getDirectoryObjectsList(fullPath);
    }

    private String getDirectoryFullPath(User user, String pathFromRequest) {
        Long userId = getCurrentUserId(user);
        return pathBuilderService.createFullDirectoryPath(userId, pathFromRequest);
    }

    private void validateCreatingDirectoryConditions(String fullPath) {
        validateParentDirectoryExists(fullPath);
        validateDirectoryNotExists(fullPath);
    }

    private void validateDirectoryExists(String fullPath) {
        if(!minioStorageService.isResourceExisting(fullPath)) {
            String pathForError = getPathForErrorMessage(fullPath);
            throw new MinioDirectoryNotExistsException(pathForError);
        }
    }

    private void putEmptyDirectoryInMinio(String fullPath) {
        minioStorageService.putEmptyDirectory(fullPath);
    }

    private DirectoryResponseDto createDirectoryResponseDto(String fullPath) {
        return new DirectoryResponseDto(
                getFormatedParentPathForResponse(fullPath),
                getDirectoryNameForResponse(fullPath),
                ResourceType.DIRECTORY
        );
    }

    private DirectoryResponseDto createDirectoryResponseDto(String pathFromRequest, String dirName) {
        return new DirectoryResponseDto(
                getNormalizedPath(pathFromRequest),
                dirName,
                ResourceType.DIRECTORY
        );
    }

    private void validateFileParentDirectoryExists(String parentPath) {
        if (!isResourceExisting(parentPath)) {
            String pathForError = getPathForErrorMessage(parentPath);
            throw new MinioExistingParentDirectoryException(pathForError);
        }
    }

    private List<FileResponseDtoDto> uploadValidatedFiles(String parentPathFromRequest, List<MultipartFile> multipartFileList, User user) {
        List<FileResponseDtoDto> fileResponseDtoList = new ArrayList<>();
        Long userId = getCurrentUserId(user);

        multipartFileList.forEach(multipartFile -> {
            String filename = multipartFile.getOriginalFilename();
            String fullFilePath = getFullFilePath(parentPathFromRequest, filename, userId);
            validateFileNotExists(fullFilePath);
            putFileInMinio(fullFilePath, multipartFile);
            fileResponseDtoList.add(
                    createFileResponseDto(fullFilePath, multipartFile.getSize())
            );
        });

        return fileResponseDtoList;
    }

    private Long getCurrentUserId(User user) {
        return currentUserService.getCurrentUserId(user);
    }

    private void validateParentDirectoryExists(String fullPath) {
        String parentPath = extractParentDirectoryPath(fullPath);

        if (!isResourceExisting(parentPath)) {
            String pathForError = getPathForErrorMessage(parentPath);
            throw new MinioExistingParentDirectoryException(pathForError);
        }
    }

    private void validateDirectoryNotExists(String fullPath) {
        if (isResourceExisting(fullPath)) {
            String pathForError = getPathForErrorMessage(fullPath);
            throw new MinioDirectoryAlreadyExistsException(pathForError);
        }
    }

    private String getPathForErrorMessage(String path) {
        return pathFormatterService.formatPathForErrorMessage(path);
    }

    private String getFullFilePath(String pathFromRequest, String filename, Long userId) {
        return pathBuilderService.createFullFilePath(userId, pathFromRequest, filename);
    }

    private void validateFileNotExists(String fullPath) {
        if (isResourceExisting(fullPath)) {
            String pathForError = getPathForErrorMessage(fullPath);
            throw new MinioUploadingResourceAlreadyExistsException(pathForError);
        }
    }

    private void putFileInMinio(String fullFilePath, MultipartFile multipartFile) {
        minioStorageService.putFile(fullFilePath, multipartFile);
    }

    private FileResponseDtoDto createFileResponseDto(String fullPath, Long size) {
        return new FileResponseDtoDto(
                getFormatedParentPathForResponse(fullPath),
                getFilenameForResponse(fullPath),
                size,
                ResourceType.FILE
        );
    }

    private FileResponseDtoDto createFileResponseDto(String pathFromRequest, String filename, Long size) {
        return new FileResponseDtoDto(
                getNormalizedPath(pathFromRequest),
                filename,
                size,
                ResourceType.FILE
        );
    }

    private String getFilenameForResponse(String fullPath) {
        return pathFormatterService.formatFilenameForResponse(fullPath);
    }

    private String getDirectoryNameForResponse(String fullPath) {
        return pathFormatterService.formatDirectoryNameForResponse(fullPath);
    }

    private String getNormalizedPath(String pathFromRequest) {
        return pathBuilderService.normalizePathFromRequest(pathFromRequest);
    }

    private String getFormatedParentPathForResponse(String fullPath) {
        return pathFormatterService.formatParentPathForResponse(fullPath);
    }

    private String extractParentDirectoryPath(String fullPath) {
        return pathFormatterService.extractParentPath(fullPath);
    }

    private boolean isResourceExisting(String path) {
        return minioStorageService.isResourceExisting(path);
    }

}
