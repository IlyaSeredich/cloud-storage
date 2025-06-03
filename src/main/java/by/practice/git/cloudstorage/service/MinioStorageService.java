package by.practice.git.cloudstorage.service;

import by.practice.git.cloudstorage.exception.CreateRootMinioDirectoryException;
import by.practice.git.cloudstorage.exception.MinioCreatingDirectoryException;
import by.practice.git.cloudstorage.exception.MinioUploadException;
import io.minio.PutObjectArgs;
import io.minio.StatObjectArgs;
import io.minio.messages.Item;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

public interface MinioStorageService {
    void putRootDirectory(String rootDirName);
    void putEmptyDirectory(String fullPath);
    void putFile(String fullFilePath, MultipartFile multipartFile);
    boolean isResourceExisting(String path);
    List<Item> getDirectoryObjectsList(String directoryPath);
}
