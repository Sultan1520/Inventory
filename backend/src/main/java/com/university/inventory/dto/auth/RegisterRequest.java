package com.university.inventory.dto.auth;

import com.university.inventory.entity.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank String fullName,
        @NotBlank @Email String email,
        @NotBlank @Size(min = 4, max = 100) String password,
        Role role,
        String position,
        String department
) {
}
