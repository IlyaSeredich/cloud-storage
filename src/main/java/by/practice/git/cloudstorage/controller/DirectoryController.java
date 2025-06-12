package by.practice.git.cloudstorage.controller;

import by.practice.git.cloudstorage.dto.BaseResourceResponseDto;
import by.practice.git.cloudstorage.service.ResourceService;
import by.practice.git.cloudstorage.validation.ValidDirectoryPath;
import by.practice.git.cloudstorage.validation.ValidPath;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/directory")
public class DirectoryController {
    private final ResourceService resourceService;

    public DirectoryController(ResourceService resourceService) {
        this.resourceService = resourceService;
    }

    @PostMapping
    public ResponseEntity<BaseResourceResponseDto> create(
            @RequestParam
            @NotBlank(message = "Param \"path\" should not be empty")
            @ValidPath(message = "Incorrect character in path: \\")
            @ValidDirectoryPath
            String path,
            @AuthenticationPrincipal
            User user
    ) {
        BaseResourceResponseDto createDirectoryResponseDto = resourceService.createEmptyDirectory(path, user);
        return new ResponseEntity<>(createDirectoryResponseDto, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<BaseResourceResponseDto>> get(
            @RequestParam
            @NotBlank(message = "Param \"path\" should not be empty")
            @ValidPath(message = "Incorrect character in path: \\")
            @ValidDirectoryPath
            String path,
            @AuthenticationPrincipal
            User user
    ) {
        List<BaseResourceResponseDto> createDirectoryResponseDto = resourceService.getDirectoryContent(path, user);
        return new ResponseEntity<>(createDirectoryResponseDto, HttpStatus.OK);
    }
}
