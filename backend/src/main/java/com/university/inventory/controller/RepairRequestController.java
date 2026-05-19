package com.university.inventory.controller;

import com.university.inventory.dto.repair.*;
import com.university.inventory.service.RepairRequestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/repair-requests")
@RequiredArgsConstructor
public class RepairRequestController {

    private final RepairRequestService repairRequestService;

    @GetMapping
    public List<RepairRequestResponse> all() {
        return repairRequestService.findAll();
    }

    @GetMapping("/{id}")
    public RepairRequestResponse byId(@PathVariable Long id) {
        return repairRequestService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RepairRequestResponse create(@Valid @RequestBody RepairRequestCreateRequest req) {
        return repairRequestService.create(req);
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN','IT_SPECIALIST')")
    public RepairRequestResponse updateStatus(@PathVariable Long id,
                                              @Valid @RequestBody RepairStatusUpdateRequest req) {
        return repairRequestService.updateStatus(id, req);
    }

    @PutMapping("/{id}/assign")
    @PreAuthorize("hasAnyRole('ADMIN','IT_SPECIALIST')")
    public RepairRequestResponse assign(@PathVariable Long id,
                                        @Valid @RequestBody RepairAssignRequest req) {
        return repairRequestService.assign(id, req);
    }

    @PostMapping("/{id}/comments")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentResponse addComment(@PathVariable Long id, @Valid @RequestBody CommentRequest req) {
        return repairRequestService.addComment(id, req);
    }

    @GetMapping("/{id}/comments")
    public List<CommentResponse> comments(@PathVariable Long id) {
        return repairRequestService.getComments(id);
    }
}
