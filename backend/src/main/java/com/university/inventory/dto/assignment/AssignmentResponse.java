package com.university.inventory.dto.assignment;

import com.university.inventory.entity.enums.AssignmentStatus;

import java.time.LocalDateTime;

public record AssignmentResponse(
        Long id,
        Long equipmentId,
        String equipmentName,
        Long assignedToUserId,
        String assignedToUserName,
        Long assignedToRoomId,
        String assignedToRoomName,
        Long assignedById,
        String assignedByName,
        LocalDateTime assignedDate,
        LocalDateTime returnDate,
        AssignmentStatus status,
        String comment
) {
}
