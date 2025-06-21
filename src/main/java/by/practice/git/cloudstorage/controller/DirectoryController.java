package by.practice.git.cloudstorage.controller;

import by.practice.git.cloudstorage.dto.BaseResourceResponseDto;
import by.practice.git.cloudstorage.dto.DirectoryResponseDto;
import by.practice.git.cloudstorage.dto.ErrorResponseDto;
import by.practice.git.cloudstorage.service.ResourceService;
import by.practice.git.cloudstorage.validation.ValidDirectoryPath;
import by.practice.git.cloudstorage.validation.ValidPath;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/directory")
@SecurityRequirement(name = "cookieAuth")
@Tag(name = "Directory API", description = "Endpoints for actions only with directories")
public class DirectoryController {
    private final ResourceService resourceService;

    public DirectoryController(ResourceService resourceService) {
        this.resourceService = resourceService;
    }

    @PostMapping
    @Operation(
            summary = "Create new directory",
            parameters = {
                    @Parameter(
                            name = "path",
                            description = "Full directories path. Must not be empty and must end with '/'",
                            example = "example-dir/example-dir2/",
                            in = ParameterIn.QUERY
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Directory successfully created.",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = DirectoryResponseDto.class),
                                    examples = {
                                            @ExampleObject(
                                                    name = "Example for created directory",
                                                    value = """
                                                                {
                                                                    "path":"example-dir/",
                                                                    "name":"example-dir2",
                                                                    "type":"DIRECTORY"
                                                                }
                                                            """
                                            )

                                    }
                            )

                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid path",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponseDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized user",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponseDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Parent path not found",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponseDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Unknown exception",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponseDto.class)
                            )
                    )
            }
    )
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
    @Operation(
            summary = "Get directory content",
            parameters = {
                    @Parameter(
                            name = "path",
                            description = "Full resources path. Must not be empty. If it is a directory, must end with '/'",
                            example = "example-dir/file.txt",
                            in = ParameterIn.QUERY
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Directory content received successfully.",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = BaseResourceResponseDto.class),
                                    examples = {
                                            @ExampleObject(
                                                    name = "Example for file",
                                                    value = """
                                                                [
                                                                    {
                                                                        "path":"example-dir2/file.txt",
                                                                        "name":"file.txt",
                                                                        "type":"FILE",
                                                                        "size":"1234"
                                                                    }
                                                                ]
                                                            """
                                            ),
                                            @ExampleObject(
                                                    name = "Example for directory",
                                                    value = """
                                                                [
                                                                    {
                                                                        "path":"example-dir/",
                                                                        "name":"example-dir2",
                                                                        "type":"DIRECTORY"
                                                                    }
                                                                ]
                                                            """
                                            )

                                    }
                            )

                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid path",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponseDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized user",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponseDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Directory not found",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponseDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Unknown exception",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponseDto.class)
                            )
                    )
            }

    )
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
