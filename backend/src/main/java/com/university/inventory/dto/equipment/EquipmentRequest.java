package com.university.inventory.dto.equipment;

import com.university.inventory.entity.enums.EquipmentCondition;
import com.university.inventory.entity.enums.EquipmentStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public record EquipmentRequest(
        @NotBlank String name,
        @NotBlank String inventoryNumber,
        String serialNumber,
        Long categoryId,
        @NotNull EquipmentStatus status,
        @NotNull EquipmentCondition condition,
        Long roomId,
        Long responsibleUserId,
        LocalDate purchaseDate,
        BigDecimal price,
        String description
) {
}
