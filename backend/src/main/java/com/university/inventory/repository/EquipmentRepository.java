package com.university.inventory.repository;

import com.university.inventory.entity.Equipment;
import com.university.inventory.entity.enums.EquipmentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface EquipmentRepository
        extends JpaRepository<Equipment, Long>, JpaSpecificationExecutor<Equipment> {

    boolean existsByInventoryNumberIgnoreCase(String inventoryNumber);

    List<Equipment> findByStatus(EquipmentStatus status);

    List<Equipment> findByResponsibleUserId(Long userId);

    long countByStatus(EquipmentStatus status);

    Page<Equipment> findTop5ByOrderByCreatedAtDesc(Pageable pageable);
}
