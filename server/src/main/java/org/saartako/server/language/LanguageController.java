package org.saartako.server.language;

import org.saartako.common.language.Language;
import org.saartako.common.language.LanguageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/language")
public class LanguageController {

    @Autowired
    private LanguageService languageService;

    @GetMapping("")
    public ResponseEntity<List<? extends Language>> findAll() {
        final List<LanguageEntity> languageEntities = this.languageService.findAll();

        final List<? extends Language> body = LanguageUtils.copy(languageEntities);

        return ResponseEntity.status(HttpStatus.OK).body(body);
    }
}
