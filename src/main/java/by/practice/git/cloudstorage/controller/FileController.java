package by.practice.git.cloudstorage.controller;

import by.practice.git.cloudstorage.dto.FileUploadDto;
import by.practice.git.cloudstorage.service.IFileService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FileController {
    private final IFileService fileService;

    public FileController(IFileService fileService) {
        this.fileService = fileService;
    }

    private void upload(
            @AuthenticationPrincipal User user,
            @RequestBody FileUploadDto fileUploadDto
    ) {
        fileService.upload(user, fileUploadDto);
    }
}
