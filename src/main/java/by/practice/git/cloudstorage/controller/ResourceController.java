package by.practice.git.cloudstorage.controller;

import by.practice.git.cloudstorage.dto.*;
import by.practice.git.cloudstorage.service.ResourceService;
import by.practice.git.cloudstorage.validation.ValidDirectoryPath;
import by.practice.git.cloudstorage.validation.ValidPath;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@SecurityRequirement(name = "cookieAuth")
@Tag(name = "Resource API", description = "Endpoints for general actions with files and directories")
public class ResourceController {
    private final ResourceService resourceService;

    public ResourceController(ResourceService resourceService) {
        this.resourceService = resourceService;
    }

    @PostMapping
    @Operation(
            summary = "Upload resource",
            parameters = {
                    @Parameter(
                            name = "path",
                            description = "Path where the uploaded files will be stored. Must not be empty and must end with a slash '/'.",
                            required = true,
                            example = "example-dir1/example-dir2/",
                            in = ParameterIn.QUERY
                    )
            },
            requestBody = @RequestBody(
                    description = "Uploading resource",
                    required = true,
                    content = @Content(
                            mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                            schema = @Schema(implementation = FileUploadDto.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Uploading completed",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(
                                            schema = @Schema(implementation = BaseResourceResponseDto.class)
                                    ),
                                    examples = {
                                            @ExampleObject(
                                                    name = "Example for uploaded file",
                                                    value = """
                                                            [
                                                                {
                                                                    "path":"example-dir/file.txt",
                                                                    "name":"file.txt",
                                                                    "type":"FILE",
                                                                    "size":"1234"
                                                                }
                                                            ]
                                                            """
                                            )
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid request body",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponseDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "Resource already exists",
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
                            responseCode = "500",
                            description = "Unknown exception",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponseDto.class)
                            )
                    )
            }

    )
    public ResponseEntity<List<FileResponseDto>> upload(
            @RequestParam
            @NotBlank(message = "Param \"path\" should not be empty")
            @ValidPath(message = "Incorrect character in path: \\")
            @ValidDirectoryPath
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
    @Operation(
            summary = "Search resource",
            parameters = {
                    @Parameter(
                            name = "query",
                            description = "Search query used to find matching resources by name",
                            required = true,
                            example = "file.txt",
                            in = ParameterIn.QUERY
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Searching completed",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(
                                            schema = @Schema(implementation = BaseResourceResponseDto.class)
                                    ),
                                    examples = {
                                            @ExampleObject(
                                                    name = "Example for file",
                                                    value = """
                                                            [
                                                                {
                                                                    "path":"example-dir/file.txt",
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
                            description = "Invalid request data",
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
                            responseCode = "500",
                            description = "Unknown error",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponseDto.class)
                            )
                    )
            }
    )
    public ResponseEntity<List<BaseResourceResponseDto>> search(
            @NotBlank(message = "Param \"query\" should not be empty")
            @RequestParam String query,
            @AuthenticationPrincipal User user
    ) {
        List<BaseResourceResponseDto> searchedContent = resourceService.getSearchedContent(query, user);
        return new ResponseEntity<>(searchedContent, HttpStatus.OK);
    }

    @GetMapping("/move")
    @Operation(
            summary = "Move/rename resource",
            description = "Resources type should math. Renaming file to directory or vice versa is forbidden",
            parameters = {
                    @Parameter(
                            name = "from",
                            description = "Full resources path. Must not be empty. If it is a directory, must end with '/'",
                            required = true,
                            example = "example-dir/file.txt",
                            in = ParameterIn.QUERY
                    ),
                    @Parameter(
                            name = "to",
                            description = "Path, that will be a new one. Must not be empty. If it is a directory, must end with '/'",
                            required = true,
                            example = "example-dir2/file.txt",
                            in = ParameterIn.QUERY
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Moving/renaming completed",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = BaseResourceResponseDto.class),
                                    examples = {
                                            @ExampleObject(
                                                    name = "Example for file",
                                                    value = """
                                                                {
                                                                    "path":"example-dir2/file.txt",
                                                                    "name":"file.txt",
                                                                    "type":"FILE",
                                                                    "size":"1234"
                                                                }
                                                            """
                                            ),
                                            @ExampleObject(
                                                    name = "Example for directory",
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
                            description = "Resource not found",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponseDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "Resource with path 'to' already exists",
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
    @Operation(
            summary = "Download resource",
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
                            description = "Downloading completed",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE,
                                    schema = @Schema(
                                            type = "string",
                                            format = "binary",
                                            description = "Binary file content"
                                    ),
                                    examples = {
                                            @ExampleObject(
                                                    name = "File download example",
                                                    summary = "A binary file is returned with Content-Type: application/octet-stream and Content-Disposition: attachment"
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
                            description = "Resource not found",
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
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + streamResourceDto.filename() + "\"")
                .body(streamResourceDto.body());
    }

    @DeleteMapping
    @Operation(
            summary = "Delete resource",
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
                            responseCode = "204",
                            description = "Resource deleted successfully. No content is returned."
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
                            description = "Resource not found",
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
    @Operation(
            summary = "Get resources details",
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
                            description = "Resource details received successfully.",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = BaseResourceResponseDto.class),
                                    examples = {
                                            @ExampleObject(
                                                    name = "Example for file",
                                                    value = """
                                                                {
                                                                    "path":"example-dir2/file.txt",
                                                                    "name":"file.txt",
                                                                    "type":"FILE",
                                                                    "size":"1234"
                                                                }
                                                            """
                                            ),
                                            @ExampleObject(
                                                    name = "Example for directory",
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
                            description = "Resource not found",
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
