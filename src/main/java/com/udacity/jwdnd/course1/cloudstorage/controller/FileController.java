package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
public class FileController {
    private final FileService fileService;
    private final UserService userService;

    public FileController(UserService userService, FileService fileService) {
        this.userService = userService;
        this.fileService = fileService;
    }

    @PostMapping("upload/file")
    public String uploadFile(@RequestParam("fileUpload") MultipartFile file, Authentication authentication, Model model) {
        Integer currentUserId = userService.getUser(authentication.getName()).getUserId();

        try {
            Integer fileId = fileService.addFile(file, currentUserId);
            if(fileId > 0){
                model.addAttribute("success", true);
                model.addAttribute("message",
                        "You successfully uploaded " + file.getOriginalFilename() + "!");
            } else{
                model.addAttribute("error", true);
                model.addAttribute("message",
                        "There was an error uploading your file " + file.getOriginalFilename() + "!");
            }
        } catch (IOException ioException){
            model.addAttribute("error", true);
            model.addAttribute("message",
                    "There was an error uploading your file " + file.getOriginalFilename() + "!");
        }
        return "result";
    }

    @GetMapping("/file/{fileId}/delete")
    public String deleteFile(@PathVariable Integer fileId, Model model) {
        try {
            fileService.deleteFile(fileId);
            model.addAttribute("success", true);
            model.addAttribute("message", "You successfully deleted file!");
        } catch (Exception e) {
            model.addAttribute("error", true);
            model.addAttribute("message", "There was an error deleting your file! ");
        }
        return "result";
    }

    @GetMapping("/file/{fileId}/download")
    public ResponseEntity<Resource> downloadFile(@PathVariable Integer fileId, Model model) {
        File file = fileService.getFileById(fileId);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(  file.getContentType() ))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFileName() + "\"")
                .body(new ByteArrayResource(file.getFileData()));
    }

}
