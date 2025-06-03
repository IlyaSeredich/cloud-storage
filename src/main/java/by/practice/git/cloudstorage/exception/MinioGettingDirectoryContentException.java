package by.practice.git.cloudstorage.exception;

public class MinioGettingDirectoryContentException extends RuntimeException {
    private static final String MESSAGE = "Getting directory content failed";

    public MinioGettingDirectoryContentException() {
        super(getErrorMessage());
    }

    public static String getErrorMessage() {
        return MESSAGE;
    }
}
