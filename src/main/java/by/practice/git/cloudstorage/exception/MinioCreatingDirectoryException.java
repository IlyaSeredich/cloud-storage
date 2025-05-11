package by.practice.git.cloudstorage.exception;

public class MinioCreatingDirectoryException extends RuntimeException {
    private static final String MESSAGE = "Creating directory %s failed";

    public MinioCreatingDirectoryException(String path) {
        super(createMessage(path));
    }

    public static String createMessage(String path) {
        return String.format(MESSAGE, path);
    }
}
