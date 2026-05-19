package com.university.inventory.controller;

import com.university.inventory.dto.assignment.AssignmentResponse;
import com.university.inventory.dto.equipment.EquipmentResponse;
import com.university.inventory.dto.repair.RepairRequestResponse;
import com.university.inventory.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/equipment-by-room")
    public Map<String, List<EquipmentResponse>> equipmentByRoom() {
        return reportService.equipmentByRoom();
    }

    @GetMapping("/equipment-in-repair")
    public List<EquipmentResponse> equipmentInRepair() {
        return reportService.equipmentInRepair();
    }

    @GetMapping("/assigned-equipment")
    public List<AssignmentResponse> assignedEquipment() {
        return reportService.assignedEquipment();
    }

    @GetMapping("/written-off-equipment")
    public List<EquipmentResponse> writtenOff() {
        return reportService.writtenOffEquipment();
    }

    @GetMapping("/repair-history")
    public List<RepairRequestResponse> repairHistory() {
        return reportService.repairHistory();
    }

    @GetMapping("/export/equipment.csv")
    public ResponseEntity<byte[]> exportEquipmentCsv() {
        byte[] body = reportService.equipmentCsv().getBytes();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=equipment.csv")
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(body);
    }
}
