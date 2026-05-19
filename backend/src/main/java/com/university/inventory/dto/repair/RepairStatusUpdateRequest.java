package com.university.inventory.dto.repair;

import com.university.inventory.entity.enums.EquipmentCondition;
import com.university.inventory.entity.enums.RequestStatus;
import jakarta.validation.constraints.NotNull;

public record RepairStatusUpdateRequest(
        @NotNull RequestStatus status,
        // optional: when closing (DONE/REJECTED) what to do with linked equipment
        EquipmentCondition resultingCondition
) {
}
