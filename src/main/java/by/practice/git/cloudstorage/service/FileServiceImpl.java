package by.practice.git.cloudstorage.service;

import by.practice.git.cloudstorage.dto.FileUploadDto;
import by.practice.git.cloudstorage.model.FileUpload;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

@Service
public class FileServiceImpl implements IFileService {
    private final String rootDir = "user-%d-files";

    public FileServiceImpl() {

    }

    @Override
    public String upload(User user, FileUploadDto fileUploadDto) {

        return "";
    }

    private void createBucket(User user) {

    }
}
