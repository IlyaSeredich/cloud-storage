package by.practice.git.cloudstorage.exception;

public class MinioDownloadResourceException extends RuntimeException {
    private static final String MESSAGE = "Downloading failed";

    public MinioDownloadResourceException() {
        super(getErrorMessage());
    }

    public static String getErrorMessage() {
        return MESSAGE;
    }
}
