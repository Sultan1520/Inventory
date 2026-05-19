package com.university.inventory.service;

import com.university.inventory.dto.assignment.AssignmentRequest;
import com.university.inventory.dto.assignment.AssignmentResponse;
import com.university.inventory.entity.Assignment;
import com.university.inventory.entity.Equipment;
import com.university.inventory.entity.enums.AssignmentStatus;
import com.university.inventory.entity.enums.EquipmentStatus;
import com.university.inventory.exception.BadRequestException;
import com.university.inventory.exception.NotFoundException;
import com.university.inventory.mapper.AssignmentMapper;
import com.university.inventory.repository.AssignmentRepository;
import com.university.inventory.repository.EquipmentRepository;
import com.university.inventory.repository.RoomRepository;
import com.university.inventory.repository.UserRepository;
import com.university.inventory.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AssignmentService {

    private final AssignmentRepository assignmentRepository;
    private final EquipmentRepository equipmentRepository;
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final AssignmentMapper assignmentMapper;

    public List<AssignmentResponse> findAll() {
        return assignmentRepository.findAll().stream().map(assignmentMapper::toResponse).toList();
    }

    public List<AssignmentResponse> findByEquipment(Long equipmentId) {
        return assignmentRepository.findByEquipmentIdOrderByAssignedDateDesc(equipmentId)
                .stream().map(assignmentMapper::toResponse).toList();
    }

    @Transactional
    public AssignmentResponse assign(AssignmentRequest req) {
        if (req.assignedToUserId() == null && req.assignedToRoomId() == null) {
            throw new BadRequestException("Either assignedToUserId or assignedToRoomId is required");
        }
        Equipment equipment = equipmentRepository.findById(req.equipmentId())
                .orElseThrow(() -> NotFoundException.of("Equipment", req.equipmentId()));
        if (equipment.getStatus() == EquipmentStatus.ASSIGNED) {
            throw new BadRequestException("Equipment is already assigned");
        }

        Assignment a = Assignment.builder()
                .equipment(equipment)
                .assignedBy(SecurityUtils.currentUser())
                .assignedDate(LocalDateTime.now())
                .status(AssignmentStatus.ACTIVE)
                .comment(req.comment())
                .build();
        if (req.assignedToUserId() != null) {
            a.setAssignedToUser(userRepository.findById(req.assignedToUserId())
                    .orElseThrow(() -> NotFoundException.of("User", req.assignedToUserId())));
        }
        if (req.assignedToRoomId() != null) {
            a.setAssignedToRoom(roomRepository.findById(req.assignedToRoomId())
                    .orElseThrow(() -> NotFoundException.of("Room", req.assignedToRoomId())));
        }

        equipment.setStatus(EquipmentStatus.ASSIGNED);
        return assignmentMapper.toResponse(assignmentRepository.save(a));
    }

    @Transactional
    public AssignmentResponse returnEquipment(Long assignmentId) {
        Assignment a = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> NotFoundException.of("Assignment", assignmentId));
        if (a.getStatus() != AssignmentStatus.ACTIVE) {
            throw new BadRequestException("Assignment is not active");
        }
        a.setStatus(AssignmentStatus.RETURNED);
        a.setReturnDate(LocalDateTime.now());
        a.getEquipment().setStatus(EquipmentStatus.AVAILABLE);
        return assignmentMapper.toResponse(a);
    }
}
