package by.practice.git.cloudstorage.service.impl;

import by.practice.git.cloudstorage.dto.BaseResourceResponseDto;
import by.practice.git.cloudstorage.dto.DirectoryResponseDto;
import by.practice.git.cloudstorage.dto.FileResponseDto;
import by.practice.git.cloudstorage.dto.FileUploadDto;
import by.practice.git.cloudstorage.exception.*;
import by.practice.git.cloudstorage.mapper.ResourceMapper;
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
    private final ResourceMapper resourceMapper;

    public MinioResourceServiceImpl(CurrentUserService currentUserService, PathBuilderService pathBuilderService, PathFormatterService pathFormatterService, MinioStorageService minioStorageService, ResourceMapper resourceMapper) {
        this.currentUserService = currentUserService;
        this.pathBuilderService = pathBuilderService;
        this.pathFormatterService = pathFormatterService;
        this.minioStorageService = minioStorageService;
        this.resourceMapper = resourceMapper;
    }

    @Override
    public void createRootDirectory(Long userId) {
        String rootDirName = getRootDirName(userId);
        putRootDirectory(rootDirName);
    }

    @Override
    public DirectoryResponseDto createEmptyDirectory(String directoryPathFromRequest, User user) {
        String fullPath = getResourceFullPath(directoryPathFromRequest, user);
        validateCreatingDirectoryConditions(fullPath);
        putEmptyDirectoryInMinio(fullPath);
        return getDirectoryResponseDto(fullPath);
    }

    @Override
    public List<FileResponseDto> uploadFiles(String parentPathFromRequest, FileUploadDto fileUploadDto, User user) {
        List<MultipartFile> multipartFileList = fileUploadDto.getMultipartFile();
        String parentPath = getResourceFullPath(parentPathFromRequest, user);
        validateFileParentDirectoryExists(parentPath);
        return uploadValidatedFiles(parentPathFromRequest, multipartFileList, user);
    }

    @Override
    public List<BaseResourceResponseDto> getDirectoryContent(String directoryPathFromRequest, User user) {
        String fullPath = getResourceFullPath(directoryPathFromRequest, user);
        validateResourceExists(fullPath);
        List<Item> minioDirectoryContentList = getMinioDirectoryContentList(fullPath);
        return createResourceResponseDtoList(minioDirectoryContentList);
    }

    @Override
    public List<BaseResourceResponseDto> getSearchedContent(String query, User user) {
        String rootDirName = getRootDirName(user);
        List<Item> wholeContentList = minioStorageService.getWholeContentList(rootDirName);
        List<Item> filteredBySearchQueryList = filterBySearchQuery(wholeContentList, query);
        return createResourceResponseDtoList(filteredBySearchQueryList);
    }

    @Override
    public BaseResourceResponseDto moveResource(String pathFrom, String pathTo, User user) {
        String fullPathFrom = getResourceFullPath(pathFrom, user);
        String fullPathTo = getResourceFullPath(pathTo, user);
        validateMovingConditions(fullPathFrom, fullPathTo);
        minioStorageService.moveResource(fullPathFrom, fullPathTo);
        return createResourceResponseDto(fullPathTo);
    }

    private void validateMovingConditions(String pathFrom, String pathTo) {
        validateResourceExists(pathFrom);
        validateParentDirectoryExists(pathTo);
        validateResourceNotExists(pathTo);
        validateResourceTypeMatches(pathFrom, pathTo);
    }

    private void validateResourceTypeMatches(String pathFrom, String pathTo) {
        if (pathFrom.endsWith("/") && !pathTo.endsWith("/") || !pathFrom.endsWith("/") && pathTo.endsWith("/")) {
            throw new MinioTypesNotMatchException(pathFrom, pathTo);
        }
    }

    private BaseResourceResponseDto createResourceResponseDto(String fullPath) {
        if(fullPath.endsWith("/")) {
            return getDirectoryResponseDto(fullPath);
        }
        long objectSize = minioStorageService.getObjectSize(fullPath);
        return getFileResponseDto(fullPath, objectSize);
    }

    private List<Item> filterBySearchQuery(List<Item> wholeContentList, String query) {
        List<Item> filteredList = new ArrayList<>();
        wholeContentList.forEach(item -> {
            String fullPath = item.objectName();
            String upperItemName = pathFormatterService.extractResourceName(fullPath).toUpperCase();
            String upperQuery = query.toUpperCase();
            if(upperItemName.contains(upperQuery)) {
                filteredList.add(item);
            }
        });
        return filteredList;
    }

    private List<BaseResourceResponseDto> createResourceResponseDtoList(List<Item> directoryObjectsList) {
        List<BaseResourceResponseDto> dtoList = new ArrayList<>();
        directoryObjectsList.forEach(item -> {
            String itemName = item.objectName();
            if(isMinioObjectDirectory(itemName)) {
                dtoList.add(getDirectoryResponseDto(itemName));
            } else {
                dtoList.add(getFileResponseDto(itemName, item.size()));
            }
        });
        return dtoList;
    }

    private String getRootDirName(Long userId) {
        return pathBuilderService.createRootDirName(userId);
    }

    private String getRootDirName(User user) {
        Long userId = getCurrentUserId(user);
        return pathBuilderService.createRootDirName(userId);
    }

    private void putRootDirectory(String rootDirName) {
        minioStorageService.putRootDirectory(rootDirName);
    }

    private List<Item> getMinioDirectoryContentList(String fullPath) {
        return minioStorageService.getDirectoryObjectsList(fullPath);
    }

    private String getResourceFullPath(String pathFromRequest, User user) {
        Long userId = getCurrentUserId(user);
        return pathBuilderService.createFullDirectoryPath(userId, pathFromRequest);
    }

    private void validateCreatingDirectoryConditions(String fullPath) {
        validateParentDirectoryExists(fullPath);
        validateResourceNotExists(fullPath);
    }

    private void validateResourceExists(String fullPath) {
        if(!minioStorageService.isResourceExisting(fullPath)) {
            String pathForError = getPathForErrorMessage(fullPath);
            throw new MinioResourceNotExistsException(pathForError);
        }
    }

    private void putEmptyDirectoryInMinio(String fullPath) {
        minioStorageService.putEmptyDirectory(fullPath);
    }

    private DirectoryResponseDto getDirectoryResponseDto(String fullPath) {
        String parentPathForResponse = getParentPathForResponse(fullPath);
        String directoryNameForResponse = getDirectoryNameForResponse(fullPath);
        return resourceMapper.createDirectoryResponseDto(parentPathForResponse, directoryNameForResponse);
    }

    private DirectoryResponseDto getDirectoryResponseDto(String pathFromRequest, String dirName) {
        String normalizedPath = getNormalizedPath(pathFromRequest);
        return resourceMapper.createDirectoryResponseDto(normalizedPath, dirName);
    }

    private void validateFileParentDirectoryExists(String parentPath) {
        if (!isResourceExisting(parentPath)) {
            String pathForError = getPathForErrorMessage(parentPath);
            throw new MinioExistingParentDirectoryException(pathForError);
        }
    }

    private List<FileResponseDto> uploadValidatedFiles(String parentPathFromRequest, List<MultipartFile> multipartFileList, User user) {
        List<FileResponseDto> fileResponseDtoList = new ArrayList<>();
        Long userId = getCurrentUserId(user);

        multipartFileList.forEach(multipartFile -> {
            String filename = multipartFile.getOriginalFilename();
            String fullFilePath = getFullFilePath(parentPathFromRequest, filename, userId);
            validateFileNotExists(fullFilePath);
            putFileInMinio(fullFilePath, multipartFile);
            fileResponseDtoList.add(
                    getFileResponseDto(fullFilePath, multipartFile.getSize())
            );
        });

        return fileResponseDtoList;
    }

    private Long getCurrentUserId(User user) {
        return currentUserService.getCurrentUserId(user);
    }

    private void validateParentDirectoryExists(String fullPath) {
        String parentPath = extractParentPath(fullPath);

        if (!isResourceExisting(parentPath)) {
            String pathForError = getPathForErrorMessage(parentPath);
            throw new MinioExistingParentDirectoryException(pathForError);
        }
    }

    private void validateResourceNotExists(String fullPath) {
        if (isResourceExisting(fullPath)) {
            String pathForError = getPathForErrorMessage(fullPath);
            throw new MinioResourceAlreadyExistsException(pathForError);
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

    private FileResponseDto getFileResponseDto(String fullPath, Long size) {
        String formatedParentPathForResponse = getParentPathForResponse(fullPath);
        String filenameForResponse = getFilenameForResponse(fullPath);
        return resourceMapper.createFileResponseDto(formatedParentPathForResponse, filenameForResponse, size);
    }

    private FileResponseDto getFileResponseDto(String pathFromRequest, String filename, Long size) {
        String normalizedPath = getNormalizedPath(pathFromRequest);
        return resourceMapper.createFileResponseDto(normalizedPath, filename, size);
    }

    private boolean isMinioObjectDirectory(String itemName) {
        return itemName.endsWith("/");
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

    private String extractParentPath(String fullPath) {
        return pathFormatterService.extractParentPath(fullPath);
    }

    private String getParentPathForResponse(String fullPath) {
        return pathFormatterService.formatParentPathForResponse(fullPath);
    }

    private boolean isResourceExisting(String path) {
        return minioStorageService.isResourceExisting(path);
    }
}
