package by.practice.git.cloudstorage.exception;

public class EmailAlreadyExistException extends RuntimeException {
    private static final String MESSAGE = "User with email %s already exists";

    public EmailAlreadyExistException(String email) {
        super(createMessage(email));
    }

    public static String createMessage(String email) {
        return String.format(MESSAGE, email);
    }

}