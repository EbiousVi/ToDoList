package com.example.ace.controller;


import com.example.ace.domain.dto.NotesDto;
import com.example.ace.domain.entity.Notes;
import com.example.ace.domain.entity.User;
import com.example.ace.service.NoteService;
import com.example.ace.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/home/notes")
public class NotesController {
    private static Long deleteId;
    private final UserService userService;
    private final NoteService noteService;

    @Autowired
    public NotesController(UserService userService, NoteService noteService) {
        this.userService = userService;
        this.noteService = noteService;
    }

    @GetMapping("/")
    public String personalNotes(Model model, Principal principal) {
        User user = userService.findUserByEmail(principal.getName());
        List<Notes> personalNotes = noteService.findAllForCurrentUserEmail(user.getEmail());
        model.addAttribute("notes", personalNotes);
        model.addAttribute("notesDto", new NotesDto());
        return "notes/notes";
    }

    @PostMapping("/")
    public String addNote(@ModelAttribute("notes") @Valid NotesDto notesDto, Principal principal) {
        noteService.saveNote(notesDto, principal);
        return "redirect:/home/notes/";
    }

    @GetMapping("/{id}")
    public String deleteNote(@PathVariable("id") Long id) {
        noteService.delete(id);
        return "redirect:/home/notes/";
    }

    @GetMapping("/user-update/{id}")
    public String updateNote(@PathVariable("id") Long id, Model model) {
        model.addAttribute("notes", noteService.findById(id));
        deleteId = id;
        return "update";
    }

    @PostMapping("/user-update")
    public String updateNotePost(@ModelAttribute("notes") @Valid NotesDto notesDto, Principal principal) {
        noteService.updateNote(notesDto, deleteId, principal);
        return "redirect:/home/notes/";
    }
}
