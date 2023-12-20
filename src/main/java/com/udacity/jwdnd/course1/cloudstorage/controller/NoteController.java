package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
public class NoteController {
    private UserService userService;
    private NoteService noteService;

    public NoteController(UserService userService, NoteService noteService) {
        this.userService = userService;
        this.noteService = noteService;
    }

    @PostMapping("/note")
    public String createOrEditNote(Note note, Authentication authentication, Model model) {
        Integer currentUserId = userService.getUser(authentication.getName()).getUserId();

        if (note.getNoteId() == null) {
            try {
                Integer noteId = noteService.addNote(note, currentUserId);
                if (noteId > 0) {
                    model.addAttribute("success", true);
                    model.addAttribute("message",
                            "You successfully added " + note.getNoteTitle() + "!");
                } else {
                    model.addAttribute("error", true);
                    model.addAttribute("message",
                            "There was an error adding your note " + note.getNoteTitle() + "!");
                }
            } catch (Exception e) {
                model.addAttribute("error", true);
                model.addAttribute("message",
                        "There was an error adding your note " + note.getNoteTitle() + "!");
            }
        } else {
            try {
                Integer noteId = noteService.editNote(note);
                if (noteId > 0) {
                    model.addAttribute("success", true);
                    model.addAttribute("message",
                            "You successfully updated " + note.getNoteTitle() + "!");
                } else {
                    model.addAttribute("error", true);
                    model.addAttribute("message",
                            "There was an error updating your note " + note.getNoteTitle() + "!");
                }
            } catch (Exception e) {
                model.addAttribute("error", true);
                model.addAttribute("message",
                        "There was an error updating your note " + note.getNoteTitle() + "!");
            }
        }
        return "result";
    }

    @GetMapping("/note/{noteId}/delete")
    public String deleteNote(@PathVariable Integer noteId, Model model) {
        try {
            noteService.deleteNote(noteId);
            model.addAttribute("success", true);
            model.addAttribute("message", "You successfully deleted note!");
        } catch (Exception e) {
            model.addAttribute("error", true);
            model.addAttribute("message", "There was an error deleting your note! ");
        }
        return "result";

    }

}
