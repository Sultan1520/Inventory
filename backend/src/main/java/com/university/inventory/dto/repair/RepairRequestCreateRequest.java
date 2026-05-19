package com.university.inventory.dto.repair;

import com.university.inventory.entity.enums.Priority;
import jakarta.validation.constraints.NotBlank;

public record RepairRequestCreateRequest(
        @NotBlank String title,
        String description,
        Long equipmentId,
        Priority priority,
        boolean markEquipmentInRepair
) {
}
