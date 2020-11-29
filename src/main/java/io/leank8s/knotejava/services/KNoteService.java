package io.leank8s.knotejava.services;

import io.leank8s.knotejava.domain.Note;
import io.leank8s.knotejava.repositories.NotesRepository;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.Collections;
import java.util.List;

@Service
public class KNoteService {

    private final NotesRepository notesRepository;

    public KNoteService(NotesRepository notesRepository) {
        this.notesRepository = notesRepository;
    }

    public void getAllNotes(Model model) {
        List<Note> notes = notesRepository.findAll();
        Collections.reverse(notes);
        model.addAttribute("notes", notes);
    }

}
