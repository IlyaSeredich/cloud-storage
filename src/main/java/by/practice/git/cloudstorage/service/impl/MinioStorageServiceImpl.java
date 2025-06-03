package by.practice.git.cloudstorage.service.impl;

import by.practice.git.cloudstorage.config.MinioProperties;
import by.practice.git.cloudstorage.exception.CreateRootMinioDirectoryException;
import by.practice.git.cloudstorage.exception.MinioCreatingDirectoryException;
import by.practice.git.cloudstorage.exception.MinioGettingDirectoryContentException;
import by.practice.git.cloudstorage.exception.MinioUploadException;
import by.practice.git.cloudstorage.service.MinioStorageService;
import io.minio.*;
import io.minio.messages.Item;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class MinioStorageServiceImpl implements MinioStorageService {
    private final PathAdapterServiceImpl pathAdapterService;
    private final MinioClient minioClient;
    private final String bucketName;

    public MinioStorageServiceImpl(PathAdapterServiceImpl pathAdapterService, MinioClient minioClient, MinioProperties minioProperties) {
        this.pathAdapterService = pathAdapterService;
        this.minioClient = minioClient;
        this.bucketName = minioProperties.getBucket();
    }

    @Override
    public void putRootDirectory(String rootDirName) {
        try (InputStream inputStream = getInputStream()) {
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(rootDirName)
                    .stream(inputStream, 0, -1)
                    .contentType("application/x-directory")
                    .build());
        } catch (Exception ex) {
            throw new CreateRootMinioDirectoryException();
        }
    }

    @Override
    public void putEmptyDirectory(String fullPath) {
        try (InputStream inputStream = getInputStream()) {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(fullPath)
                            .stream(inputStream, 0, -1)
                            .contentType("application/x-directory")
                            .build()
            );
        } catch (Exception ex) {
            String pathForError = getPathForErrorMessage(fullPath);
            throw new MinioCreatingDirectoryException(pathForError);
        }
    }

    @Override
    public void putFile(String fullFilePath, MultipartFile multipartFile) {
        try (InputStream inputStream = multipartFile.getInputStream()) {
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(fullFilePath)
                    .stream(inputStream, multipartFile.getSize(), -1)
                    .build());
        } catch (Exception ex) {
            throw new MinioUploadException();
        }
    }

    @Override
    public boolean isResourceExisting(String path) {
        try {
            minioClient.statObject(
                    StatObjectArgs.builder()
                            .bucket(bucketName)
                            .object(path)
                            .build()
            );
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    @Override
    public List<Item> getDirectoryObjectsList(String directoryPath) {
        Iterable<Result<Item>> directoryObjects = getDirectoryObjects(directoryPath);
        return convertDirectoryObjectsToList(directoryObjects, directoryPath);
    }

    private Iterable<Result<Item>> getDirectoryObjects(String directoryPath) {
        return minioClient.listObjects(
                ListObjectsArgs.builder()
                        .bucket(bucketName)
                        .prefix(directoryPath)
                        .delimiter("/")
                        .build()
        );
    }

    private List<Item> convertDirectoryObjectsToList(Iterable<Result<Item>> directoryObjects, String directoryPath) {
        List<Item> itemList = new ArrayList<>();

        directoryObjects.forEach(itemResult -> {
                    Item item = null;
                    try {
                        item = itemResult.get();
                    } catch (Exception ex) {
                        throw new MinioGettingDirectoryContentException();
                    }
                    if (!item.objectName().equals(directoryPath)) {
                        itemList.add(item);
                    }
                }
        );
        return itemList;
    }

    private InputStream getInputStream() {
        return new ByteArrayInputStream(new byte[0]);
    }

    private String getPathForErrorMessage(String path) {
        return pathAdapterService.formatPathForErrorMessage(path);
    }
}
