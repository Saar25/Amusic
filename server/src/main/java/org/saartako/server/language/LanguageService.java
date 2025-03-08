package org.saartako.server.language;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LanguageService {

    @Autowired
    private LanguageRepository languageRepository;

    public List<LanguageEntity> findAll() {
        return this.languageRepository.findAll();
    }
}
