package by.practice.git.cloudstorage.service;

import jakarta.servlet.http.HttpServletRequest;

public interface AuthenticationService {
    void authenticate(String username, String password, HttpServletRequest request);
}
