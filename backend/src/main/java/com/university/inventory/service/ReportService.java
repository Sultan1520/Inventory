package com.university.inventory.service;

import com.university.inventory.dto.assignment.AssignmentResponse;
import com.university.inventory.dto.equipment.EquipmentResponse;
import com.university.inventory.dto.repair.RepairRequestResponse;
import com.university.inventory.entity.enums.AssignmentStatus;
import com.university.inventory.entity.enums.EquipmentStatus;
import com.university.inventory.mapper.AssignmentMapper;
import com.university.inventory.mapper.EquipmentMapper;
import com.university.inventory.mapper.RepairMapper;
import com.university.inventory.repository.AssignmentRepository;
import com.university.inventory.repository.EquipmentRepository;
import com.university.inventory.repository.RepairRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReportService {

    private final EquipmentRepository equipmentRepository;
    private final AssignmentRepository assignmentRepository;
    private final RepairRequestRepository repairRequestRepository;
    private final EquipmentMapper equipmentMapper;
    private final AssignmentMapper assignmentMapper;
    private final RepairMapper repairMapper;

    public Map<String, List<EquipmentResponse>> equipmentByRoom() {
        return equipmentRepository.findAll().stream()
                .map(equipmentMapper::toResponse)
                .collect(Collectors.groupingBy(
                        e -> e.roomName() != null ? e.roomName() : "Unassigned"));
    }

    public List<EquipmentResponse> equipmentInRepair() {
        return equipmentRepository.findByStatus(EquipmentStatus.IN_REPAIR)
                .stream().map(equipmentMapper::toResponse).toList();
    }

    public List<EquipmentResponse> writtenOffEquipment() {
        return equipmentRepository.findByStatus(EquipmentStatus.WRITTEN_OFF)
                .stream().map(equipmentMapper::toResponse).toList();
    }

    public List<AssignmentResponse> assignedEquipment() {
        return assignmentRepository.findByStatus(AssignmentStatus.ACTIVE)
                .stream().map(assignmentMapper::toResponse).toList();
    }

    public List<RepairRequestResponse> repairHistory() {
        return repairRequestRepository.findAll()
                .stream().map(repairMapper::toResponse).toList();
    }

    public String equipmentCsv() {
        StringBuilder sb = new StringBuilder();
        sb.append("Id,Name,InventoryNumber,SerialNumber,Category,Status,Condition,Room,Responsible,Price\n");
        equipmentRepository.findAll().forEach(e -> sb
                .append(e.getId()).append(',')
                .append(csv(e.getName())).append(',')
                .append(csv(e.getInventoryNumber())).append(',')
                .append(csv(e.getSerialNumber())).append(',')
                .append(csv(e.getCategory() != null ? e.getCategory().getName() : "")).append(',')
                .append(e.getStatus()).append(',')
                .append(e.getCondition()).append(',')
                .append(csv(e.getRoom() != null ? e.getRoom().getName() : "")).append(',')
                .append(csv(e.getResponsibleUser() != null ? e.getResponsibleUser().getFullName() : "")).append(',')
                .append(e.getPrice() != null ? e.getPrice() : "")
                .append('\n'));
        return sb.toString();
    }

    private String csv(String v) {
        if (v == null) return "";
        if (v.contains(",") || v.contains("\"") || v.contains("\n")) {
            return "\"" + v.replace("\"", "\"\"") + "\"";
        }
        return v;
    }
}
