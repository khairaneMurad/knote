package io.leank8s.knotejava.controllers;

import io.leank8s.knotejava.configuration.KnoteProperties;
import io.leank8s.knotejava.domain.Note;
import io.leank8s.knotejava.repositories.NotesRepository;
import io.leank8s.knotejava.services.KNoteService;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;

@Controller
public class KNoteController {

    private final KNoteService kNoteService;
    private final NotesRepository notesRepository;
    private Parser parser;
    private HtmlRenderer renderer;

    @Autowired
    private KnoteProperties properties;

    public KNoteController(KNoteService kNoteService, NotesRepository notesRepository) {
        this.kNoteService = kNoteService;
        this.notesRepository = notesRepository;
        this.parser = Parser.builder().build();
        this.renderer = HtmlRenderer.builder().build();
    }

    @GetMapping("/")
    public String index(Model model) {
        kNoteService.getAllNotes(model);
        return "index";
    }

    @PostMapping("/note")
    public String saveNotes(@RequestParam("image") MultipartFile file,
                            @RequestParam String description,
                            @RequestParam(required = false) String publish,
                            @RequestParam(required = false) String upload,
                            Model model) throws Exception {

        if (publish != null && publish.equals("Publish")) {
            saveNote(description, model);
            kNoteService.getAllNotes(model);
            return "redirect:/";
        }

        if (upload != null && upload.equals("Upload")) {
            if (file != null && file.getOriginalFilename() != null
                    && !file.getOriginalFilename().isEmpty()) {
                uploadImage(file, description, model);
            }
            kNoteService.getAllNotes(model);
            return "index";
        }
        // After save fetch all notes again
        return "index";
    }

    private void saveNote(String description, Model model) {
        if (description != null && !description.trim().isEmpty()) {
            Node document = parser.parse(description.trim());
            String html = renderer.render(document);
            notesRepository.save(new Note(null, html));
            //After publish you need to clean up the textarea
            model.addAttribute("description", "");
        }
    }

    private void uploadImage(MultipartFile file, String description, Model model) throws Exception {
        File uploadsDir = new File(properties.getUploadDir());
        if (!uploadsDir.exists()) {
            uploadsDir.mkdir();
        }
        String fileId = UUID.randomUUID().toString() + "." +
                file.getOriginalFilename().split("\\.")[1];
        file.transferTo(new File(properties.getUploadDir() + fileId));
        model.addAttribute("description",
                description + " ![](/uploads/" + fileId + ")");
    }



}
