package by.practice.git.cloudstorage.exception;

public class MinioDirectoryNotExistsException extends RuntimeException {
    private static final String MESSAGE_TEMPLATE = "Directory %s not exists";

    public MinioDirectoryNotExistsException(String fullPath) {
        super(createErrorMessage(fullPath));
    }

    public static String createErrorMessage(String fullPath) {
        return String.format(MESSAGE_TEMPLATE, fullPath);
    }
}
