package com.university.inventory.dto.repair;

import java.time.LocalDateTime;

public record CommentResponse(
        Long id,
        Long repairRequestId,
        Long authorId,
        String authorName,
        String text,
        LocalDateTime createdAt
) {
}
