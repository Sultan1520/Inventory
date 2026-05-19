package com.university.inventory.controller;

import com.university.inventory.dto.assignment.AssignmentRequest;
import com.university.inventory.dto.assignment.AssignmentResponse;
import com.university.inventory.service.AssignmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/assignments")
@RequiredArgsConstructor
public class AssignmentController {

    private final AssignmentService assignmentService;

    @GetMapping
    public List<AssignmentResponse> all() {
        return assignmentService.findAll();
    }

    @GetMapping("/equipment/{equipmentId}")
    public List<AssignmentResponse> byEquipment(@PathVariable Long equipmentId) {
        return assignmentService.findByEquipment(equipmentId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('ADMIN','IT_SPECIALIST')")
    public AssignmentResponse assign(@Valid @RequestBody AssignmentRequest req) {
        return assignmentService.assign(req);
    }

    @PutMapping("/{id}/return")
    @PreAuthorize("hasAnyRole('ADMIN','IT_SPECIALIST')")
    public AssignmentResponse returnEquipment(@PathVariable Long id) {
        return assignmentService.returnEquipment(id);
    }
}
