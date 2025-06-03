package by.practice.git.cloudstorage.service;

public interface PathFormatterService {
    String formatPathForErrorMessage(String path);
    String formatParentPathForResponse(String fullPath);
    String formatDirectoryNameForResponse(String fullPath);
    String formatFilenameForResponse(String fullPath);
    String extractParentPath(String fullPath);
}
