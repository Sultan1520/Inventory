package com.university.inventory.repository;

import com.university.inventory.entity.RepairRequest;
import com.university.inventory.entity.enums.RequestStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RepairRequestRepository extends JpaRepository<RepairRequest, Long> {

    List<RepairRequest> findByCreatedByIdOrderByCreatedAtDesc(Long userId);

    long countByStatus(RequestStatus status);

    Page<RepairRequest> findAllByOrderByCreatedAtDesc(Pageable pageable);
}
