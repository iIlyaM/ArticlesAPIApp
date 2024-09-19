package ru.ilyam.articlesapi.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;
import ru.ilyam.articlesapi.entity.Privilege;
import ru.ilyam.articlesapi.repository.PrivilegeRepository;
import ru.ilyam.articlesapi.service.PrivilegeService;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class PrivilegeServiceImpl implements PrivilegeService {
    private final PrivilegeRepository privilegeRepository;

    public boolean isValidPrivileges(UserDetails userDetails, HttpServletRequest request) {
        String path = request.getRequestURI();
        AntPathMatcher matcher = new AntPathMatcher();
        for (var authority : userDetails.getAuthorities()) {
            Set<Privilege> tempPrivileges = privilegeRepository.findAllByRoleName(authority.getAuthority());
            for (var privilege : tempPrivileges) {
                if (matcher.match(privilege.getEndpoint(), path)) {
                    return true;
                }
            }
        }
        return false;
    }
}
