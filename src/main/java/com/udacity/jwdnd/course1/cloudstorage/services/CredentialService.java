package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.mapper.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class CredentialService {
    private final CredentialMapper credentialMapper;
    private EncryptionService encryptionService;

    public CredentialService(CredentialMapper credentialMapper, EncryptionService encryptionService) {
        this.credentialMapper = credentialMapper;
        this.encryptionService = encryptionService;
    }

    public Integer addCredential(Credential credential, Integer userId) throws IOException {
        String key = encryptionService.generateKey();
        credential.setKey(key);
        credential.setUserid(userId);
        credential.setPassword(encryptPassword(credential));

        return credentialMapper.insert(credential);
    }
    public String encryptPassword(Credential credential) {
        return this.encryptionService.encryptValue(credential.getPassword(), credential.getKey());
    }

    public Integer editCredential(Credential credential) {
        Credential cre = credentialMapper.getCredentialByCredentialid(credential.getCredentialid());
        credential.setKey(cre.getKey());
        credential.setPassword(encryptPassword(credential));
        return credentialMapper.update(credential);
    }

    public void deleteCredential(Integer credentialId) {
        credentialMapper.delete(credentialId);
    }

    public List<Credential> getCredentialsForUser(Integer userId) {
        return credentialMapper.getCredentialsByUserId(userId);
    }
}
