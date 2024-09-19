package ru.ilyam.articlesapi.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.userdetails.UserDetails;

public interface PrivilegeService {

    boolean isValidPrivileges(UserDetails userDetails, HttpServletRequest request);
}
