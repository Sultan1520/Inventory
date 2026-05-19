package com.university.inventory.dto.dashboard;

import com.university.inventory.dto.equipment.EquipmentResponse;
import com.university.inventory.dto.repair.RepairRequestResponse;

import java.util.List;
import java.util.Map;

public record DashboardSummary(
        long totalEquipment,
        long available,
        long assigned,
        long inRepair,
        long writtenOff,
        long lost,
        long totalRequests,
        long newRequests,
        long inProgressRequests,
        long doneRequests,
        Map<String, Long> equipmentByCategory,
        Map<String, Long> equipmentByStatus,
        List<RepairRequestResponse> latestRequests,
        List<EquipmentResponse> latestEquipment
) {
}
