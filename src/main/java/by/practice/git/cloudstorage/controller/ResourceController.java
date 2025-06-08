package by.practice.git.cloudstorage.controller;

import by.practice.git.cloudstorage.dto.*;
import by.practice.git.cloudstorage.dto.DirectoryResponseDto;
import by.practice.git.cloudstorage.dto.BaseResourceResponseDto;
import by.practice.git.cloudstorage.service.ResourceService;
import by.practice.git.cloudstorage.validation.ValidPath;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@Validated
public class ResourceController {
    private final ResourceService resourceService;

    public ResourceController(ResourceService resourceService) {
        this.resourceService = resourceService;
    }

    @PostMapping("/resource")
    public ResponseEntity<List<FileResponseDto>> upload(
            @RequestParam
            @Size(max = 255, message = "Max path params size = 255")
            @NotBlank(message = "Param path should not be empty")
            @ValidPath(message = "Incorrect characters in path: <>,:,\",|,?,*,..")
            String path,
            @ModelAttribute
            FileUploadDto fileUploadDto,
            @AuthenticationPrincipal
            User user
    ) {
        List<FileResponseDto> responseDto = resourceService.uploadFiles(path, fileUploadDto, user);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @PostMapping("/directory")
    public ResponseEntity<BaseResourceResponseDto> createDirectory(
            @RequestParam
            @Size(max = 255, message = "Max path params size = 255")
            @NotBlank(message = "Param path should not be empty")
            @ValidPath(message = "Incorrect characters in path: <>,:,\",|,?,*, ,..")
            String path,
            @AuthenticationPrincipal
            User user
    ) {
        BaseResourceResponseDto createDirectoryResponseDto = resourceService.createEmptyDirectory(path, user);
        return new ResponseEntity<>(createDirectoryResponseDto, HttpStatus.CREATED);
    }

    @GetMapping("/directory")
    public ResponseEntity<List<BaseResourceResponseDto>> getDirectoryContent(
            @RequestParam
            @Size(max = 255, message = "Max path params size = 255")
            @NotBlank(message = "Param path should not be empty")
            @ValidPath(message = "Incorrect characters in path")
            String path,
            @AuthenticationPrincipal
            User user
    ) {
        List<BaseResourceResponseDto> createDirectoryResponseDto = resourceService.getDirectoryContent(path, user);
        return new ResponseEntity<>(createDirectoryResponseDto, HttpStatus.OK);
    }

    //TODO fix slash at the end of the path

    @GetMapping("/resource/search")
    public ResponseEntity<List<BaseResourceResponseDto>> searchResources(
            @NotBlank(message = "Param query should not be empty")
            @RequestParam String query,
            @AuthenticationPrincipal User user
    ) {
        List<BaseResourceResponseDto> searchedContent = resourceService.getSearchedContent(query, user);
        return new ResponseEntity<>(searchedContent, HttpStatus.OK);
    }

    @GetMapping("/resource/move")
    public ResponseEntity<BaseResourceResponseDto> moveResource(
            @NotBlank(message = "Param \"from\" should not be empty")
            @RequestParam
            String from,
            @NotBlank(message = "Param \"to\" should not be empty")
            @RequestParam
            String to,
            @AuthenticationPrincipal
            User user
    ) {
        BaseResourceResponseDto baseResourceResponseDto = resourceService.moveResource(from, to, user);
        return new ResponseEntity<>(baseResourceResponseDto, HttpStatus.OK);
    }


}
