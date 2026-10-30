package org.ghizlane.aiagentservice.dto;

import jakarta.validation.constraints.NotBlank;

public record ChatRequest(

        @NotBlank(
                message = "The message is required"
        )
        String message

) {
}