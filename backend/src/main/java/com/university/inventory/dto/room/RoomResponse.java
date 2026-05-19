package com.university.inventory.dto.room;

public record RoomResponse(
        Long id,
        String name,
        String building,
        Integer floor,
        String type,
        String description
) {
}
