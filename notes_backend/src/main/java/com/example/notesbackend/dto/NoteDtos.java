package com.example.notesbackend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.Instant;

/**
 * Note request/response DTOs.
 */
public class NoteDtos {

    public static class CreateNoteRequest {
        @Schema(description = "Title of the note", example = "Grocery List")
        @NotBlank @Size(max = 200)
        public String title;

        @Schema(description = "Content/body of the note", example = "Milk, Eggs, Bread")
        @Size(max = 5000)
        public String content;
    }

    public static class UpdateNoteRequest {
        @Schema(description = "Title of the note", example = "Updated title")
        @NotBlank @Size(max = 200)
        public String title;

        @Schema(description = "Content/body of the note", example = "Updated content")
        @Size(max = 5000)
        public String content;
    }

    public static class NoteResponse {
        public Long id;
        public String title;
        public String content;
        public Instant createdAt;
        public Instant updatedAt;

        public NoteResponse(Long id, String title, String content, Instant createdAt, Instant updatedAt) {
            this.id = id; this.title = title; this.content = content; this.createdAt = createdAt; this.updatedAt = updatedAt;
        }
    }
}
