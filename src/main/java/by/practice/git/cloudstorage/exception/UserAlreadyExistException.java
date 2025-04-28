package by.practice.git.cloudstorage.exception;

public class UserAlreadyExistException extends RuntimeException {
    private static final String MESSAGE = "User with username %s already exists";

    public UserAlreadyExistException(String username) {
        super(createMessage(username));
    }

    public static String createMessage(String username) {
        return String.format(MESSAGE, username);
    }

}