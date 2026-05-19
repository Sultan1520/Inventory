package com.university.inventory.dto.repair;

import jakarta.validation.constraints.NotBlank;

public record CommentRequest(@NotBlank String text) {
}
