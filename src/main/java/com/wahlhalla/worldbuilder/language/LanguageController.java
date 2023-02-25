package com.wahlhalla.worldbuilder.language;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wahlhalla.worldbuilder.util.exceptions.EntityNotFoundException;

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
    
}
