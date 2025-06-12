package by.practice.git.cloudstorage.controller;

import by.practice.git.cloudstorage.dto.BaseResourceResponseDto;
import by.practice.git.cloudstorage.dto.FileResponseDto;
import by.practice.git.cloudstorage.dto.FileUploadDto;
import by.practice.git.cloudstorage.dto.StreamResourceDto;
import by.practice.git.cloudstorage.service.ResourceService;
import by.practice.git.cloudstorage.validation.ValidPath;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.util.List;

@RestController
@RequestMapping("/api/resource")
@Validated
public class ResourceController {
    private final ResourceService resourceService;

    public ResourceController(ResourceService resourceService) {
        this.resourceService = resourceService;
    }

    @PostMapping
    public ResponseEntity<List<FileResponseDto>> upload(
            @RequestParam
            @NotBlank(message = "Param \"path\" should not be empty")
            @ValidPath(message = "Incorrect character in path: \\")
            String path,
            @ModelAttribute
            FileUploadDto fileUploadDto,
            @AuthenticationPrincipal
            User user
    ) {
        List<FileResponseDto> responseDto = resourceService.uploadFiles(path, fileUploadDto, user);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @GetMapping("/search")
    public ResponseEntity<List<BaseResourceResponseDto>> search(
            @NotBlank(message = "Param \"query\" should not be empty")
            @RequestParam String query,
            @AuthenticationPrincipal User user
    ) {
        List<BaseResourceResponseDto> searchedContent = resourceService.getSearchedContent(query, user);
        return new ResponseEntity<>(searchedContent, HttpStatus.OK);
    }

    @GetMapping("/move")
    public ResponseEntity<BaseResourceResponseDto> move(
            @NotBlank(message = "Param \"from\" should not be empty")
            @ValidPath(message = "Incorrect character in path: \\")
            @RequestParam
            String from,
            @NotBlank(message = "Param \"to\" should not be empty")
            @ValidPath(message = "Incorrect character in path: \\")
            @RequestParam
            String to,
            @AuthenticationPrincipal
            User user
    ) {
        BaseResourceResponseDto baseResourceResponseDto = resourceService.moveResource(from, to, user);
        return new ResponseEntity<>(baseResourceResponseDto, HttpStatus.OK);
    }

    @GetMapping("/download")
    public ResponseEntity<StreamingResponseBody> download(
            @NotBlank(message = "Param \"path\" should not be empty")
            @ValidPath(message = "Incorrect character in path: \\")
            @RequestParam
            String path,
            @AuthenticationPrincipal
            User user
    ) {
        StreamResourceDto streamResourceDto = resourceService.downloadResource(path, user);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + streamResourceDto.getFilename() + "\"")
                .body(streamResourceDto.getBody());
    }

    @DeleteMapping
    public ResponseEntity<Void> delete(
            @RequestParam
            @NotBlank(message = "Param \"path\" should not be empty")
            @ValidPath(message = "Incorrect character in path: \\")
            String path,
            @AuthenticationPrincipal
            User user
    ) {
        resourceService.deleteResource(path, user);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<BaseResourceResponseDto> get(
            @RequestParam
            @NotBlank(message = "Param \"path\" should not be empty")
            @ValidPath(message = "Incorrect character in path: \\")
            String path,
            @AuthenticationPrincipal
            User user
    ) {
        BaseResourceResponseDto baseResourceResponseDto = resourceService.getResourceInfo(path, user);
        return new ResponseEntity<>(baseResourceResponseDto, HttpStatus.OK);
    }
}
