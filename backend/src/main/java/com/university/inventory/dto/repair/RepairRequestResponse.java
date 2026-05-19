package com.university.inventory.dto.repair;

import com.university.inventory.entity.enums.Priority;
import com.university.inventory.entity.enums.RequestStatus;

import java.time.LocalDateTime;

public record RepairRequestResponse(
        Long id,
        String title,
        String description,
        Long equipmentId,
        String equipmentName,
        Long createdById,
        String createdByName,
        Long assignedToId,
        String assignedToName,
        RequestStatus status,
        Priority priority,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        LocalDateTime closedAt
) {
}
