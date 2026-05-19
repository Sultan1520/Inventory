package com.university.inventory.service;

import com.university.inventory.dto.common.PageResponse;
import com.university.inventory.dto.equipment.EquipmentRequest;
import com.university.inventory.dto.equipment.EquipmentResponse;
import com.university.inventory.entity.Category;
import com.university.inventory.entity.Equipment;
import com.university.inventory.entity.Room;
import com.university.inventory.entity.User;
import com.university.inventory.entity.enums.EquipmentStatus;
import com.university.inventory.exception.ConflictException;
import com.university.inventory.exception.NotFoundException;
import com.university.inventory.mapper.EquipmentMapper;
import com.university.inventory.repository.CategoryRepository;
import com.university.inventory.repository.EquipmentRepository;
import com.university.inventory.repository.RoomRepository;
import com.university.inventory.repository.UserRepository;
import com.university.inventory.service.spec.EquipmentSpecifications;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EquipmentService {

    private final EquipmentRepository equipmentRepository;
    private final CategoryRepository categoryRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final EquipmentMapper equipmentMapper;

    public PageResponse<EquipmentResponse> search(String term, Long categoryId, Long roomId,
                                                  EquipmentStatus status, Pageable pageable) {
        Specification<Equipment> spec = EquipmentSpecifications.combine(
                EquipmentSpecifications.search(term),
                EquipmentSpecifications.hasCategory(categoryId),
                EquipmentSpecifications.hasRoom(roomId),
                EquipmentSpecifications.hasStatus(status));
        Page<EquipmentResponse> page = equipmentRepository.findAll(spec, pageable)
                .map(equipmentMapper::toResponse);
        return PageResponse.of(page);
    }

    public EquipmentResponse findById(Long id) {
        return equipmentMapper.toResponse(get(id));
    }

    @Transactional
    public EquipmentResponse create(EquipmentRequest req) {
        if (equipmentRepository.existsByInventoryNumberIgnoreCase(req.inventoryNumber())) {
            throw new ConflictException("Inventory number already exists: " + req.inventoryNumber());
        }
        Equipment e = new Equipment();
        apply(e, req);
        return equipmentMapper.toResponse(equipmentRepository.save(e));
    }

    @Transactional
    public EquipmentResponse update(Long id, EquipmentRequest req) {
        Equipment e = get(id);
        if (!e.getInventoryNumber().equalsIgnoreCase(req.inventoryNumber())
                && equipmentRepository.existsByInventoryNumberIgnoreCase(req.inventoryNumber())) {
            throw new ConflictException("Inventory number already exists: " + req.inventoryNumber());
        }
        apply(e, req);
        return equipmentMapper.toResponse(e);
    }

    @Transactional
    public void delete(Long id) {
        equipmentRepository.delete(get(id));
    }

    private void apply(Equipment e, EquipmentRequest req) {
        e.setName(req.name());
        e.setInventoryNumber(req.inventoryNumber());
        e.setSerialNumber(req.serialNumber());
        e.setStatus(req.status());
        e.setCondition(req.condition());
        e.setPurchaseDate(req.purchaseDate());
        e.setPrice(req.price());
        e.setDescription(req.description());
        e.setCategory(req.categoryId() == null ? null : categoryRepository.findById(req.categoryId())
                .orElseThrow(() -> NotFoundException.of("Category", req.categoryId())));
        e.setRoom(req.roomId() == null ? null : roomRepository.findById(req.roomId())
                .orElseThrow(() -> NotFoundException.of("Room", req.roomId())));
        e.setResponsibleUser(req.responsibleUserId() == null ? null
                : userRepository.findById(req.responsibleUserId())
                .orElseThrow(() -> NotFoundException.of("User", req.responsibleUserId())));
    }

    private Equipment get(Long id) {
        return equipmentRepository.findById(id)
                .orElseThrow(() -> NotFoundException.of("Equipment", id));
    }
}
