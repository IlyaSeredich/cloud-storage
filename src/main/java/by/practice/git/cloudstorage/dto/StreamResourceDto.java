package by.practice.git.cloudstorage.dto;

import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

public class StreamResourceDto {
    private final StreamingResponseBody body;
    private final String filename;

    public StreamResourceDto(StreamingResponseBody body, String filename) {
        this.body = body;
        this.filename = filename;
    }

    public StreamingResponseBody getBody() {
        return body;
    }

    public String getFilename() {
        return filename;
    }
}
