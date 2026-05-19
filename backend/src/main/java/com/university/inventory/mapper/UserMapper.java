package com.university.inventory.mapper;

import com.university.inventory.dto.user.UserResponse;
import com.university.inventory.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserResponse toResponse(User u) {
        if (u == null) return null;
        return new UserResponse(
                u.getId(),
                u.getFullName(),
                u.getEmail(),
                u.getRole(),
                u.getPosition(),
                u.getDepartment(),
                u.isEnabled(),
                u.getCreatedAt()
        );
    }
}
