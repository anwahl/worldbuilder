package com.wahlhalla.worldbuilder.language;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.wahlhalla.worldbuilder.util.exceptions.EntityIdMismatchException;
import com.wahlhalla.worldbuilder.util.exceptions.EntityNotFoundException;

import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/api/language")
public class LanguageController {
    
    @Autowired
    private LanguageRepository languageRepository;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<Language> findAll() {
        return this.languageRepository.findAll();
    }

    @GetMapping("/public")
    public List<Language> findAllPublic(@PathVariable Long worldId) {
        return this.languageRepository.findByWorldIdAndWorldIsPrivateFalse(worldId);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @checkUser.byLanguageId(authentication, #id)"
                   + " or @checkPrivacy.byLanguageId(#id)")
    public Language findOne(@PathVariable Long id) {
        return this.languageRepository.findById(id)
          .orElseThrow(EntityNotFoundException::new);
    }

    @GetMapping("/world/{worldId}")
    @PreAuthorize("hasRole('ADMIN') or @checkUser.byWorldId(authentication, #worldId)"
                  + " or @checkPrivacy.byWorldId(#worldId)")
    public List<Language> findByWorldId(@PathVariable Long worldId) {
        return this.languageRepository.findByWorldId(worldId);
    }

    @GetMapping("/name/{name}")
    @PreAuthorize("hasRole('ADMIN')")
    public List<Language> findByName(@PathVariable String name) {
        return this.languageRepository.findByNameContainingIgnoreCase(name);
    }

    @Transactional
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('USER')")
    public Language create(@RequestBody Language language) {
        return this.languageRepository.save(language);
    }

    @Transactional
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @checkUser.byLanguageId(authentication, #id)")
    public void delete(@PathVariable Long id) {
        this.languageRepository.findById(id)
          .orElseThrow(EntityNotFoundException::new);
        this.languageRepository.deleteById(id);
    }

    @Transactional
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @checkUser.byLanguageId(authentication, #id)")
    public Language update(@RequestBody Language language, @PathVariable Long id) {
        if (language.getId() != id) {
          throw new EntityIdMismatchException();
        }
        this.languageRepository.findById(id)
          .orElseThrow(EntityNotFoundException::new);
        return this.languageRepository.save(language);
    }
}
