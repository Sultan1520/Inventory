package com.university.inventory.dto.repair;

import jakarta.validation.constraints.NotNull;

public record RepairAssignRequest(@NotNull Long assignedToUserId) {
}
