package com.university.inventory.service;

import com.university.inventory.dto.room.RoomRequest;
import com.university.inventory.dto.room.RoomResponse;
import com.university.inventory.entity.Room;
import com.university.inventory.exception.NotFoundException;
import com.university.inventory.mapper.RoomMapper;
import com.university.inventory.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final RoomMapper roomMapper;

    public List<RoomResponse> findAll() {
        return roomRepository.findAll().stream().map(roomMapper::toResponse).toList();
    }

    public RoomResponse findById(Long id) {
        return roomMapper.toResponse(get(id));
    }

    @Transactional
    public RoomResponse create(RoomRequest req) {
        Room r = Room.builder()
                .name(req.name())
                .building(req.building())
                .floor(req.floor())
                .type(req.type())
                .description(req.description())
                .build();
        return roomMapper.toResponse(roomRepository.save(r));
    }

    @Transactional
    public RoomResponse update(Long id, RoomRequest req) {
        Room r = get(id);
        r.setName(req.name());
        r.setBuilding(req.building());
        r.setFloor(req.floor());
        r.setType(req.type());
        r.setDescription(req.description());
        return roomMapper.toResponse(r);
    }

    @Transactional
    public void delete(Long id) {
        roomRepository.delete(get(id));
    }

    private Room get(Long id) {
        return roomRepository.findById(id)
                .orElseThrow(() -> NotFoundException.of("Room", id));
    }
}
