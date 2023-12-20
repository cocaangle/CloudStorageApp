package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class CredentialController {
    private UserService userService;
    private CredentialService credentialService;

    public CredentialController(UserService userService, CredentialService credentialService) {
        this.userService = userService;
        this.credentialService = credentialService;
    }

    @PostMapping("/credential")
    public String createOrEditCredential(Credential credential, Authentication authentication, Model model) {
        Integer currentUserId = userService.getUser(authentication.getName()).getUserId();
        Logger logger = LoggerFactory.getLogger("mira");
        if (credential.getCredentialid() == null) {
            try {
                Integer credentialId = credentialService.addCredential(credential, currentUserId);
                if (credentialId > 0) {
                    model.addAttribute("success", true);
                    model.addAttribute("message",
                            "You successfully added credential!");
                } else {
                    model.addAttribute("error", true);
                    model.addAttribute("message",
                            "There was an error adding your credential!" );
                }
            } catch (Exception e) {
                model.addAttribute("error", true);
                model.addAttribute("message",
                        "There was an error adding your credential!");
            }
        } else {
            try {
                Integer credentialId = credentialService.editCredential(credential);
                if (credentialId > 0) {
                    model.addAttribute("success", true);
                    model.addAttribute("message",
                            "You successfully updated credential!");
                } else {
                    logger.info("credentialId negative: {}", credentialId);
                    model.addAttribute("error", true);
                    model.addAttribute("message",
                            "There was an error updating your credential!");
                }
            } catch (Exception e) {
                logger.info("edit credential error{}", e,getClass().getName());
                model.addAttribute("error", true);
                model.addAttribute("message",
                        "There was an error updating your credential!");
            }
        }
        return "result";
    }

    @GetMapping("/credential/{credentialid}/delete")
    public String deleteCredential(@PathVariable Integer credentialid, Model model) {
        try {
            credentialService.deleteCredential(credentialid);
            model.addAttribute("success", true);
            model.addAttribute("message", "You successfully deleted credential!");
        } catch (Exception e) {
            model.addAttribute("error", true);
            model.addAttribute("message", "There was an error deleting your credential! ");
        }
        return "result";
    }

}
