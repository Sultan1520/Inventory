package com.university.inventory.dto.user;

import com.university.inventory.entity.enums.Role;
import jakarta.validation.constraints.NotNull;

public record UpdateRoleRequest(@NotNull Role role) {
}
