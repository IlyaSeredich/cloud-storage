package by.practice.git.cloudstorage.exception;

public class MinioBucketInitializationException extends RuntimeException {
    private static final String MESSAGE_TEMPLATE = "Failed initialization minio bucket";

    public MinioBucketInitializationException(
    ) {
        super(createMessage());
    }

    public static String createMessage() {
        return MESSAGE_TEMPLATE;
    }
}
