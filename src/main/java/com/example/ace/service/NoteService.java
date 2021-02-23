package com.example.ace.service;


import com.example.ace.domain.dto.NotesDto;
import com.example.ace.domain.entity.Notes;
import com.example.ace.repository.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
public class NoteService {
    private final NoteRepository noteRepository;
    private final UserService userService;

    @Autowired
    public NoteService(UserService userService, NoteRepository noteRepository) {
        this.userService = userService;
        this.noteRepository = noteRepository;
    }

    public void saveNote(NotesDto notesDto, Principal principal) {
        Notes notes = new Notes();
        notes.setNote1(notesDto.getNote1());
        notes.setNote2(notesDto.getNote2());
        notes.setNote3(notesDto.getNote3());
        notes.setUser(userService.findUserByEmail(principal.getName()));
        noteRepository.save(notes);
    }

    public void updateNote(NotesDto notesDto, Long id, Principal principal) {
        Notes notes = new Notes();
        notes.setNote1(notesDto.getNote1());
        notes.setNote2(notesDto.getNote2());
        notes.setNote3(notesDto.getNote3());
        notes.setId(id);
        notes.setUser(userService.findUserByEmail(principal.getName()));
        noteRepository.save(notes);
    }

    public List<Notes> findAllForCurrentUserEmail(String email) {
        return noteRepository.findAllByUserEmail(email);
    }

    public void delete(Long id) {
        noteRepository.deleteById(id);
    }

    public Notes findById(Long id) {
        return noteRepository.getOne(id);
    }
}
