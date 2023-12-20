package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.mapper.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class NoteService {
    private final NoteMapper noteMapper;
    public NoteService(NoteMapper noteMapper) {
        this.noteMapper = noteMapper;
    }

    public Integer addNote(Note note, Integer userId) throws IOException {
        Note newNote = new Note(null, note.getNoteTitle(), note.getNoteDescription(), userId);
        return noteMapper.insert(newNote);
    }

    public List<Note> getNotesForUser(Integer userId) {
        return noteMapper.getNotesByUserId(userId);
    }

    public void deleteNote(Integer noteId) {
        noteMapper.deleteNote(noteId);
    }

    public Integer editNote(Note note) {
        return noteMapper.updateNote(note);
    }
}
