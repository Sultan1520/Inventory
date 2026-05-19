package com.university.inventory.mapper;

import com.university.inventory.dto.repair.CommentResponse;
import com.university.inventory.dto.repair.RepairRequestResponse;
import com.university.inventory.entity.RepairComment;
import com.university.inventory.entity.RepairRequest;
import org.springframework.stereotype.Component;

@Component
public class RepairMapper {

    public RepairRequestResponse toResponse(RepairRequest r) {
        if (r == null) return null;
        return new RepairRequestResponse(
                r.getId(),
                r.getTitle(),
                r.getDescription(),
                r.getEquipment() != null ? r.getEquipment().getId() : null,
                r.getEquipment() != null ? r.getEquipment().getName() : null,
                r.getCreatedBy() != null ? r.getCreatedBy().getId() : null,
                r.getCreatedBy() != null ? r.getCreatedBy().getFullName() : null,
                r.getAssignedTo() != null ? r.getAssignedTo().getId() : null,
                r.getAssignedTo() != null ? r.getAssignedTo().getFullName() : null,
                r.getStatus(),
                r.getPriority(),
                r.getCreatedAt(),
                r.getUpdatedAt(),
                r.getClosedAt()
        );
    }

    public CommentResponse toResponse(RepairComment c) {
        if (c == null) return null;
        return new CommentResponse(
                c.getId(),
                c.getRepairRequest() != null ? c.getRepairRequest().getId() : null,
                c.getAuthor() != null ? c.getAuthor().getId() : null,
                c.getAuthor() != null ? c.getAuthor().getFullName() : null,
                c.getText(),
                c.getCreatedAt()
        );
    }
}
