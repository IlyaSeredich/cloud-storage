package by.practice.git.cloudstorage.exception;

public class InvalidUploadingFilenameException extends RuntimeException {
    private static final String MESSAGE_TEMPLATE = "Filename %s contains prohibited symbols \":, *, ?, <, >, |, %%, #, {, }, &, ~, =, $, !, ', `, @, +, ^, (, ), [, ], ;, ,, ?\"";

    public InvalidUploadingFilenameException(String filename) {
        super(createErrorMessage(filename));
    }

    public static String createErrorMessage(String filename) {
        return String.format(MESSAGE_TEMPLATE, filename);
    }
}
