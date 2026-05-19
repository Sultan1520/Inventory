package com.university.inventory.repository;

import com.university.inventory.entity.RepairComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RepairCommentRepository extends JpaRepository<RepairComment, Long> {

    List<RepairComment> findByRepairRequestIdOrderByCreatedAtAsc(Long repairRequestId);
}
