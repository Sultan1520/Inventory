package com.university.inventory.service;

import com.university.inventory.dto.user.UpdateRoleRequest;
import com.university.inventory.dto.user.UpdateStatusRequest;
import com.university.inventory.dto.user.UserResponse;
import com.university.inventory.entity.User;
import com.university.inventory.exception.NotFoundException;
import com.university.inventory.mapper.UserMapper;
import com.university.inventory.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public List<UserResponse> findAll() {
        return userRepository.findAll().stream().map(userMapper::toResponse).toList();
    }

    public UserResponse findById(Long id) {
        return userMapper.toResponse(getUser(id));
    }

    @Transactional
    public UserResponse updateRole(Long id, UpdateRoleRequest req) {
        User user = getUser(id);
        user.setRole(req.role());
        return userMapper.toResponse(user);
    }

    @Transactional
    public UserResponse updateStatus(Long id, UpdateStatusRequest req) {
        User user = getUser(id);
        user.setEnabled(req.enabled());
        return userMapper.toResponse(user);
    }

    private User getUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> NotFoundException.of("User", id));
    }
}
