package by.practice.git.cloudstorage.exception;

public class MinioDirectoryAlreadyExistsException extends RuntimeException {
    private static final String MESSAGE = "Directory %s already exists";

    public MinioDirectoryAlreadyExistsException(String fullPath) {
        super(createMessage(fullPath));
    }

    public static String createMessage(String fullPath) {
        return String.format(MESSAGE, fullPath);
    }
}
