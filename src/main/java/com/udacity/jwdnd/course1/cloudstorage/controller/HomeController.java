package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.services.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class HomeController {
    private final FileService fileService;
    private final UserService userService;
    private final NoteService noteService;
    private final CredentialService credentialService;
    private final EncryptionService encryptionService;

    public HomeController(FileService fileService, UserService userService, NoteService noteService, CredentialService credentialService, EncryptionService encryptionService) {
        this.fileService = fileService;
        this.userService = userService;
        this.noteService = noteService;
        this.credentialService = credentialService;
        this.encryptionService = encryptionService;
    }

    @GetMapping("/home")
    public String getHomeView(Authentication authentication, Model model) {
        Integer currentUserId = userService.getUser(authentication.getName()).getUserId();
        model.addAttribute("files", fileService.getFilesForUser(currentUserId));
        model.addAttribute("notes", noteService.getNotesForUser(currentUserId));
        model.addAttribute("credentials", credentialService.getCredentialsForUser(currentUserId));
        model.addAttribute("encryptionService", encryptionService);
        return "home";
    }

    @PostMapping("/logout")
    public String logOut() {
        return "login";
    }



}
