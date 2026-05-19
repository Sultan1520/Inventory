package com.university.inventory.controller;

import com.university.inventory.dto.user.UpdateRoleRequest;
import com.university.inventory.dto.user.UpdateStatusRequest;
import com.university.inventory.dto.user.UserResponse;
import com.university.inventory.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<UserResponse> all() {
        return userService.findAll();
    }

    @GetMapping("/{id}")
    public UserResponse byId(@PathVariable Long id) {
        return userService.findById(id);
    }

    @PutMapping("/{id}/role")
    public UserResponse updateRole(@PathVariable Long id, @Valid @RequestBody UpdateRoleRequest req) {
        return userService.updateRole(id, req);
    }

    @PutMapping("/{id}/status")
    public UserResponse updateStatus(@PathVariable Long id, @Valid @RequestBody UpdateStatusRequest req) {
        return userService.updateStatus(id, req);
    }
}
