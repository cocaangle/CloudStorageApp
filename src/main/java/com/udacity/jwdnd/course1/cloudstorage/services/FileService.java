package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class FileService {

    private final FileMapper fileMapper;

    public FileService(FileMapper fileMapper) {
        this.fileMapper = fileMapper;
    }

    public Integer addFile(MultipartFile multipartFile, Integer userId) throws IOException {
        File file = new File(null, multipartFile.getOriginalFilename(), multipartFile.getContentType(), multipartFile.getSize(), userId, multipartFile.getBytes());
        return fileMapper.insert(file);
    }

    public List<File> getFilesForUser(Integer userId) {
        return fileMapper.getFilesByUserId(userId);
    }

    public File getFileById(Integer fileId) {return fileMapper.getFileById(fileId);}

    public void deleteFile(Integer fileId) {fileMapper.deleteFileById(fileId); }

}
