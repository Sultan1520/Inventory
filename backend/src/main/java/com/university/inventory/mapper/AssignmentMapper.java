package com.university.inventory.mapper;

import com.university.inventory.dto.assignment.AssignmentResponse;
import com.university.inventory.entity.Assignment;
import org.springframework.stereotype.Component;

@Component
public class AssignmentMapper {

    public AssignmentResponse toResponse(Assignment a) {
        if (a == null) return null;
        return new AssignmentResponse(
                a.getId(),
                a.getEquipment() != null ? a.getEquipment().getId() : null,
                a.getEquipment() != null ? a.getEquipment().getName() : null,
                a.getAssignedToUser() != null ? a.getAssignedToUser().getId() : null,
                a.getAssignedToUser() != null ? a.getAssignedToUser().getFullName() : null,
                a.getAssignedToRoom() != null ? a.getAssignedToRoom().getId() : null,
                a.getAssignedToRoom() != null ? a.getAssignedToRoom().getName() : null,
                a.getAssignedBy() != null ? a.getAssignedBy().getId() : null,
                a.getAssignedBy() != null ? a.getAssignedBy().getFullName() : null,
                a.getAssignedDate(),
                a.getReturnDate(),
                a.getStatus(),
                a.getComment()
        );
    }
}
