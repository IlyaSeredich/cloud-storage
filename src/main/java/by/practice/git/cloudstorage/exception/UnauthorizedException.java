package by.practice.git.cloudstorage.exception;

public class UnauthorizedException extends RuntimeException{
    private static final String MESSAGE_TEMPLATE = "Full authentication is required";

    public UnauthorizedException() {
        super(createMessage());
    }

    public static String createMessage() {
        return MESSAGE_TEMPLATE;
    }
}
