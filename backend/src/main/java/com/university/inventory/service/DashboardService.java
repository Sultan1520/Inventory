package com.university.inventory.service;

import com.university.inventory.dto.dashboard.DashboardSummary;
import com.university.inventory.entity.Equipment;
import com.university.inventory.entity.enums.EquipmentStatus;
import com.university.inventory.entity.enums.RequestStatus;
import com.university.inventory.mapper.EquipmentMapper;
import com.university.inventory.mapper.RepairMapper;
import com.university.inventory.repository.EquipmentRepository;
import com.university.inventory.repository.RepairRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DashboardService {

    private final EquipmentRepository equipmentRepository;
    private final RepairRequestRepository repairRequestRepository;
    private final EquipmentMapper equipmentMapper;
    private final RepairMapper repairMapper;

    public DashboardSummary summary() {
        long total = equipmentRepository.count();

        Map<String, Long> byStatus = new LinkedHashMap<>();
        for (EquipmentStatus s : EquipmentStatus.values()) {
            byStatus.put(s.name(), equipmentRepository.countByStatus(s));
        }

        Map<String, Long> byCategory = equipmentRepository.findAll().stream()
                .collect(Collectors.groupingBy(
                        e -> e.getCategory() != null ? e.getCategory().getName() : "Uncategorized",
                        Collectors.counting()));

        return new DashboardSummary(
                total,
                equipmentRepository.countByStatus(EquipmentStatus.AVAILABLE),
                equipmentRepository.countByStatus(EquipmentStatus.ASSIGNED),
                equipmentRepository.countByStatus(EquipmentStatus.IN_REPAIR),
                equipmentRepository.countByStatus(EquipmentStatus.WRITTEN_OFF),
                equipmentRepository.countByStatus(EquipmentStatus.LOST),
                repairRequestRepository.count(),
                repairRequestRepository.countByStatus(RequestStatus.NEW),
                repairRequestRepository.countByStatus(RequestStatus.IN_PROGRESS),
                repairRequestRepository.countByStatus(RequestStatus.DONE),
                byCategory,
                byStatus,
                repairRequestRepository.findAllByOrderByCreatedAtDesc(PageRequest.of(0, 5))
                        .map(repairMapper::toResponse).getContent(),
                equipmentRepository.findTop5ByOrderByCreatedAtDesc(PageRequest.of(0, 5))
                        .map(equipmentMapper::toResponse).getContent()
        );
    }
}
