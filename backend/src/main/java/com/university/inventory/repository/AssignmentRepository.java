package com.university.inventory.repository;

import com.university.inventory.entity.Assignment;
import com.university.inventory.entity.enums.AssignmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AssignmentRepository extends JpaRepository<Assignment, Long> {

    List<Assignment> findByEquipmentIdOrderByAssignedDateDesc(Long equipmentId);

    Optional<Assignment> findFirstByEquipmentIdAndStatus(Long equipmentId, AssignmentStatus status);

    List<Assignment> findByStatus(AssignmentStatus status);
}
