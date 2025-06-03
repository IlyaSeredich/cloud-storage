package by.practice.git.cloudstorage.exception;

public class MinioUploadException extends RuntimeException{
    private static final String MESSAGE = "Uploading failed";

    public MinioUploadException() {
        super(getErrorMessage());
    }

    public static String getErrorMessage() {
        return MESSAGE;
    }
}
