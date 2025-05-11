package by.practice.git.cloudstorage.exception;

public class MinioExistingParentDirectoryException extends RuntimeException {
    private static final String MESSAGE = "Parent directory %s not found";

    public MinioExistingParentDirectoryException(String path) {
        super(createMessage(path));
    }

    public static String createMessage(String path) {
        return String.format(MESSAGE, path);
    }
}
