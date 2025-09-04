package com.example.notesbackend.service;

import com.example.notesbackend.model.Note;
import com.example.notesbackend.model.User;
import com.example.notesbackend.repository.NoteRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Note service providing CRUD operations with ownership checks.
 */
@Service
public class NoteService {

    private final NoteRepository noteRepository;

    @Autowired
    public NoteService(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    // PUBLIC_INTERFACE
    @Transactional
    public Note create(User owner, String title, String content) {
        Note n = new Note(title, content, owner);
        return noteRepository.save(n);
    }

    // PUBLIC_INTERFACE
    public Page<Note> list(User owner, int page, int size, String sortBy, Sort.Direction direction) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        return noteRepository.findAllByOwner(owner, pageable);
    }

    // PUBLIC_INTERFACE
    public Note getOwned(User owner, Long id) {
        Note note = noteRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Note not found"));
        if (!note.getOwner().getId().equals(owner.getId())) {
            throw new SecurityException("Access denied");
        }
        return note;
    }

    // PUBLIC_INTERFACE
    @Transactional
    public Note update(User owner, Long id, String title, String content) {
        Note n = getOwned(owner, id);
        n.setTitle(title);
        n.setContent(content);
        return noteRepository.save(n);
    }

    // PUBLIC_INTERFACE
    @Transactional
    public void delete(User owner, Long id) {
        Note n = getOwned(owner, id);
        noteRepository.delete(n);
    }
}
