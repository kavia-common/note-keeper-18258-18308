package com.example.notesbackend.controller;

import com.example.notesbackend.dto.NoteDtos;
import com.example.notesbackend.model.Note;
import com.example.notesbackend.model.User;
import com.example.notesbackend.service.NoteService;
import com.example.notesbackend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

/**
 * CRUD endpoints for Notes.
 */
@RestController
@RequestMapping("/api/notes")
@Tag(name = "Notes", description = "Operations for creating, reading, updating, and deleting notes")
public class NoteController {

    private final NoteService noteService;
    private final UserService userService;

    public NoteController(NoteService noteService, UserService userService) {
        this.noteService = noteService;
        this.userService = userService;
    }

    private User resolveUser(UserDetails principal) {
        return userService.getByUsername(principal.getUsername());
    }

    // PUBLIC_INTERFACE
    @PostMapping
    @Operation(summary = "Create a note", description = "Creates a note for the authenticated user.")
    public ResponseEntity<NoteDtos.NoteResponse> create(@AuthenticationPrincipal UserDetails principal,
                                                        @Valid @RequestBody NoteDtos.CreateNoteRequest req) {
        User user = resolveUser(principal);
        Note n = noteService.create(user, req.title, req.content);
        return ResponseEntity.ok(toDto(n));
    }

    // PUBLIC_INTERFACE
    @GetMapping
    @Operation(summary = "List notes", description = "Lists notes for the authenticated user with pagination.")
    public ResponseEntity<Page<NoteDtos.NoteResponse>> list(@AuthenticationPrincipal UserDetails principal,
                                                            @RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "10") int size,
                                                            @RequestParam(defaultValue = "updatedAt") String sortBy,
                                                            @RequestParam(defaultValue = "DESC") Sort.Direction direction) {
        User user = resolveUser(principal);
        Page<Note> notes = noteService.list(user, page, size, sortBy, direction);
        Page<NoteDtos.NoteResponse> mapped = notes.map(this::toDto);
        return ResponseEntity.ok(mapped);
    }

    // PUBLIC_INTERFACE
    @GetMapping("/{id}")
    @Operation(summary = "Get note", description = "Retrieves a single note by id for the authenticated user.")
    public ResponseEntity<?> get(@AuthenticationPrincipal UserDetails principal, @PathVariable Long id) {
        User user = resolveUser(principal);
        try {
            Note n = noteService.getOwned(user, id);
            return ResponseEntity.ok(toDto(n));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (SecurityException se) {
            return ResponseEntity.status(403).body("Forbidden");
        }
    }

    // PUBLIC_INTERFACE
    @PutMapping("/{id}")
    @Operation(summary = "Update note", description = "Updates an existing note owned by the authenticated user.")
    public ResponseEntity<?> update(@AuthenticationPrincipal UserDetails principal,
                                    @PathVariable Long id,
                                    @Valid @RequestBody NoteDtos.UpdateNoteRequest req) {
        User user = resolveUser(principal);
        try {
            Note n = noteService.update(user, id, req.title, req.content);
            return ResponseEntity.ok(toDto(n));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (SecurityException se) {
            return ResponseEntity.status(403).body("Forbidden");
        }
    }

    // PUBLIC_INTERFACE
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete note", description = "Deletes a note owned by the authenticated user.")
    public ResponseEntity<?> delete(@AuthenticationPrincipal UserDetails principal,
                                    @PathVariable Long id) {
        User user = resolveUser(principal);
        try {
            noteService.delete(user, id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (SecurityException se) {
            return ResponseEntity.status(403).body("Forbidden");
        }
    }

    private NoteDtos.NoteResponse toDto(Note n) {
        return new NoteDtos.NoteResponse(n.getId(), n.getTitle(), n.getContent(), n.getCreatedAt(), n.getUpdatedAt());
    }
}
