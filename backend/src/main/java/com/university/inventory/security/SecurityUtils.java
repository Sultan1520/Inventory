package com.university.inventory.security;

import com.university.inventory.entity.User;
import com.university.inventory.exception.BadRequestException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public final class SecurityUtils {

    private SecurityUtils() {
    }

    public static User currentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof AppUserDetails details)) {
            throw new BadRequestException("No authenticated user in context");
        }
        return details.getUser();
    }
}
