package by.practice.git.cloudstorage.dto;


import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;


public class ErrorResponseDto {
    private final String message;
    private final String error;
    private final int status;
    private final String path;
    private final LocalDateTime dateTime;

    public ErrorResponseDto(String message, HttpStatus httpStatus, String path) {
        this.message = message;
        this.error = httpStatus.getReasonPhrase();
        this.status = httpStatus.value();
        this.path = path;
        this.dateTime = LocalDateTime.now();
    }

    public String getMessage() {
        return message;
    }

    public String getError() {
        return error;
    }

    public int getStatus() {
        return status;
    }

    public String getPath() {
        return path;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }
}
