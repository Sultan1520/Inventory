package com.university.inventory.dto.user;

import com.university.inventory.entity.enums.Role;

import java.time.LocalDateTime;

public record UserResponse(
        Long id,
        String fullName,
        String email,
        Role role,
        String position,
        String department,
        boolean enabled,
        LocalDateTime createdAt
) {
}
