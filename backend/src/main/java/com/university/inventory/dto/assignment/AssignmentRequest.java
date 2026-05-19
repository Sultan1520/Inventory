package com.university.inventory.dto.assignment;

import jakarta.validation.constraints.NotNull;

public record AssignmentRequest(
        @NotNull Long equipmentId,
        Long assignedToUserId,
        Long assignedToRoomId,
        String comment
) {
}
