package com.university.inventory.mapper;

import com.university.inventory.dto.equipment.EquipmentResponse;
import com.university.inventory.entity.Equipment;
import org.springframework.stereotype.Component;

@Component
public class EquipmentMapper {

    public EquipmentResponse toResponse(Equipment e) {
        if (e == null) return null;
        return new EquipmentResponse(
                e.getId(),
                e.getName(),
                e.getInventoryNumber(),
                e.getSerialNumber(),
                e.getCategory() != null ? e.getCategory().getId() : null,
                e.getCategory() != null ? e.getCategory().getName() : null,
                e.getStatus(),
                e.getCondition(),
                e.getRoom() != null ? e.getRoom().getId() : null,
                e.getRoom() != null ? e.getRoom().getName() : null,
                e.getResponsibleUser() != null ? e.getResponsibleUser().getId() : null,
                e.getResponsibleUser() != null ? e.getResponsibleUser().getFullName() : null,
                e.getPurchaseDate(),
                e.getPrice(),
                e.getDescription(),
                e.getCreatedAt(),
                e.getUpdatedAt()
        );
    }
}
