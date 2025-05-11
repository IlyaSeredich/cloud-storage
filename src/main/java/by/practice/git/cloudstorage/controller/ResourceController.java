package by.practice.git.cloudstorage.controller;

import by.practice.git.cloudstorage.dto.CreateDirectoryResponseDto;
import by.practice.git.cloudstorage.dto.FileUploadDto;
import by.practice.git.cloudstorage.service.IResourceService;
import by.practice.git.cloudstorage.validation.ValidPath;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@Validated
public class ResourceController {
    private final IResourceService fileService;

    public ResourceController(IResourceService fileService) {
        this.fileService = fileService;
    }

    public void upload(
            @RequestBody FileUploadDto fileUploadDto
    ) {
        fileService.upload(fileUploadDto);
    }

    @PostMapping("/directory")
    public ResponseEntity<CreateDirectoryResponseDto> createDirectory(
            @RequestParam
            @Size(max = 255, message = "Max path params size = 255")
            @NotBlank(message = "Param path should not be empty")
            @ValidPath(message = "Incorrect characters in path: <>,:,\",|,?,*,..")
            String path,
            @AuthenticationPrincipal User user
    ) {
        CreateDirectoryResponseDto createDirectoryResponseDto = fileService.createEmptyDir(user, path);
        return new ResponseEntity<>(createDirectoryResponseDto, HttpStatus.CREATED);
    }


}
