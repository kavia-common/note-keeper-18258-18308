package com.example.notesbackend.repository;

import com.example.notesbackend.model.Note;
import com.example.notesbackend.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for Note.
 */
public interface NoteRepository extends JpaRepository<Note, Long> {
    Page<Note> findAllByOwner(User owner, Pageable pageable);
}
