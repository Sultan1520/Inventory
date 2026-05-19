package com.university.inventory.controller;

import com.university.inventory.dto.common.PageResponse;
import com.university.inventory.dto.equipment.EquipmentRequest;
import com.university.inventory.dto.equipment.EquipmentResponse;
import com.university.inventory.entity.enums.EquipmentStatus;
import com.university.inventory.service.EquipmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/equipment")
@RequiredArgsConstructor
public class EquipmentController {

    private final EquipmentService equipmentService;

    @GetMapping
    public PageResponse<EquipmentResponse> list(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long roomId,
            @RequestParam(required = false) EquipmentStatus status,
            @PageableDefault(size = 10, sort = "id") Pageable pageable) {
        return equipmentService.search(search, categoryId, roomId, status, pageable);
    }

    @GetMapping("/{id}")
    public EquipmentResponse byId(@PathVariable Long id) {
        return equipmentService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public EquipmentResponse create(@Valid @RequestBody EquipmentRequest req) {
        return equipmentService.create(req);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public EquipmentResponse update(@PathVariable Long id, @Valid @RequestBody EquipmentRequest req) {
        return equipmentService.update(id, req);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable Long id) {
        equipmentService.delete(id);
    }
}
