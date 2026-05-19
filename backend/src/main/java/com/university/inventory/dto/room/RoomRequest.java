package com.university.inventory.dto.room;

import jakarta.validation.constraints.NotBlank;

public record RoomRequest(
        @NotBlank String name,
        String building,
        Integer floor,
        String type,
        String description
) {
}
