package by.practice.git.cloudstorage.exception;

public class MinioMovingException extends RuntimeException {
    private static final String MESSAGE = "Moving failed";

    public MinioMovingException() {
        super(getErrorMessage());
    }

    public static String getErrorMessage() {
        return MESSAGE;
    }
}
