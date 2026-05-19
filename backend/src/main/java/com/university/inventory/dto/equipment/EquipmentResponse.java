package com.university.inventory.dto.equipment;

import com.university.inventory.entity.enums.EquipmentCondition;
import com.university.inventory.entity.enums.EquipmentStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record EquipmentResponse(
        Long id,
        String name,
        String inventoryNumber,
        String serialNumber,
        Long categoryId,
        String categoryName,
        EquipmentStatus status,
        EquipmentCondition condition,
        Long roomId,
        String roomName,
        Long responsibleUserId,
        String responsibleUserName,
        LocalDate purchaseDate,
        BigDecimal price,
        String description,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
