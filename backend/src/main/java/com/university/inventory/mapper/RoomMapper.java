package com.university.inventory.mapper;

import com.university.inventory.dto.room.RoomResponse;
import com.university.inventory.entity.Room;
import org.springframework.stereotype.Component;

@Component
public class RoomMapper {

    public RoomResponse toResponse(Room r) {
        if (r == null) return null;
        return new RoomResponse(
                r.getId(), r.getName(), r.getBuilding(),
                r.getFloor(), r.getType(), r.getDescription());
    }
}
