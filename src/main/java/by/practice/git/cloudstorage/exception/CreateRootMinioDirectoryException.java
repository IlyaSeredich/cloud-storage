package by.practice.git.cloudstorage.exception;

public class CreateRootMinioDirectoryException extends RuntimeException {
    private static final String MESSAGE = "Failed creating root Minio directory";

    public CreateRootMinioDirectoryException() {
        super(createMessage());
    }

    public static String createMessage() {
        return MESSAGE;
    }
}
