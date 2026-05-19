package com.university.inventory.service;

import com.university.inventory.dto.repair.*;
import com.university.inventory.entity.Equipment;
import com.university.inventory.entity.RepairComment;
import com.university.inventory.entity.RepairRequest;
import com.university.inventory.entity.User;
import com.university.inventory.entity.enums.EquipmentCondition;
import com.university.inventory.entity.enums.EquipmentStatus;
import com.university.inventory.entity.enums.Priority;
import com.university.inventory.entity.enums.RequestStatus;
import com.university.inventory.entity.enums.Role;
import com.university.inventory.exception.NotFoundException;
import com.university.inventory.mapper.RepairMapper;
import com.university.inventory.repository.EquipmentRepository;
import com.university.inventory.repository.RepairCommentRepository;
import com.university.inventory.repository.RepairRequestRepository;
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
public class RepairRequestService {

    private final RepairRequestRepository repairRequestRepository;
    private final RepairCommentRepository repairCommentRepository;
    private final EquipmentRepository equipmentRepository;
    private final UserRepository userRepository;
    private final RepairMapper repairMapper;

    public List<RepairRequestResponse> findAll() {
        User current = SecurityUtils.currentUser();
        List<RepairRequest> list;
        if (current.getRole() == Role.TEACHER) {
            list = repairRequestRepository.findByCreatedByIdOrderByCreatedAtDesc(current.getId());
        } else {
            list = repairRequestRepository.findAll();
        }
        return list.stream().map(repairMapper::toResponse).toList();
    }

    public RepairRequestResponse findById(Long id) {
        return repairMapper.toResponse(get(id));
    }

    @Transactional
    public RepairRequestResponse create(RepairRequestCreateRequest req) {
        RepairRequest r = RepairRequest.builder()
                .title(req.title())
                .description(req.description())
                .createdBy(SecurityUtils.currentUser())
                .status(RequestStatus.NEW)
                .priority(req.priority() != null ? req.priority() : Priority.MEDIUM)
                .build();
        if (req.equipmentId() != null) {
            Equipment e = equipmentRepository.findById(req.equipmentId())
                    .orElseThrow(() -> NotFoundException.of("Equipment", req.equipmentId()));
            r.setEquipment(e);
            if (req.markEquipmentInRepair()) {
                e.setStatus(EquipmentStatus.IN_REPAIR);
            }
        }
        return repairMapper.toResponse(repairRequestRepository.save(r));
    }

    @Transactional
    public RepairRequestResponse assign(Long id, RepairAssignRequest req) {
        RepairRequest r = get(id);
        User assignee = userRepository.findById(req.assignedToUserId())
                .orElseThrow(() -> NotFoundException.of("User", req.assignedToUserId()));
        r.setAssignedTo(assignee);
        if (r.getStatus() == RequestStatus.NEW) {
            r.setStatus(RequestStatus.IN_PROGRESS);
        }
        return repairMapper.toResponse(r);
    }

    @Transactional
    public RepairRequestResponse updateStatus(Long id, RepairStatusUpdateRequest req) {
        RepairRequest r = get(id);
        r.setStatus(req.status());
        if (req.status() == RequestStatus.DONE || req.status() == RequestStatus.REJECTED) {
            r.setClosedAt(LocalDateTime.now());
            Equipment e = r.getEquipment();
            if (e != null && e.getStatus() == EquipmentStatus.IN_REPAIR) {
                EquipmentCondition cond = req.resultingCondition();
                if (cond == EquipmentCondition.BROKEN || cond == EquipmentCondition.NEEDS_REPAIR) {
                    e.setCondition(cond);
                    e.setStatus(EquipmentStatus.AVAILABLE);
                } else {
                    if (cond != null) e.setCondition(cond);
                    e.setStatus(EquipmentStatus.AVAILABLE);
                }
            }
        }
        return repairMapper.toResponse(r);
    }

    @Transactional
    public CommentResponse addComment(Long id, CommentRequest req) {
        RepairRequest r = get(id);
        RepairComment c = RepairComment.builder()
                .repairRequest(r)
                .author(SecurityUtils.currentUser())
                .text(req.text())
                .build();
        return repairMapper.toResponse(repairCommentRepository.save(c));
    }

    public List<CommentResponse> getComments(Long id) {
        get(id);
        return repairCommentRepository.findByRepairRequestIdOrderByCreatedAtAsc(id)
                .stream().map(repairMapper::toResponse).toList();
    }

    private RepairRequest get(Long id) {
        return repairRequestRepository.findById(id)
                .orElseThrow(() -> NotFoundException.of("RepairRequest", id));
    }
}
