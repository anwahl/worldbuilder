package com.wahlhalla.worldbuilder.religion;


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

@RestController
@RequestMapping("/api/religion")
public class ReligionController {
    
    @Autowired
    private ReligionRepository religionRepository;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<Religion> findAll() {
        return this.religionRepository.findAll();
    }

    @GetMapping("/public")
    public List<Religion> findAllPublic(@PathVariable Long worldId) {
        return this.religionRepository.findByWorldIdAndWorldIsPrivateFalse(worldId);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @checkUser.byReligionId(authentication, #id)"
                   + " or @checkPrivacy.byReligionId(#id)")
    public Religion findOne(@PathVariable Long id) {
        return this.religionRepository.findById(id)
          .orElseThrow(EntityNotFoundException::new);
    }

    @GetMapping("/world/{worldId}")
    @PreAuthorize("hasRole('ADMIN') or @checkUser.byWorldId(authentication, #worldId)"
                  + " or @checkPrivacy.byWorldId(#worldId)")
    public List<Religion> findByWorldId(@PathVariable Long worldId) {
        return this.religionRepository.findByWorldId(worldId);
    }

    @GetMapping("/name/{name}")
    @PreAuthorize("hasRole('ADMIN')")
    public List<Religion> findByName(@PathVariable String name) {
        return this.religionRepository.findByNameContainingIgnoreCase(name);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('USER')")
    public Religion create(@RequestBody Religion religion) {
        return this.religionRepository.save(religion);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @checkUser.byReligionId(authentication, #id)")
    public void delete(@PathVariable Long id) {
        this.religionRepository.findById(id)
          .orElseThrow(EntityNotFoundException::new);
        this.religionRepository.deleteById(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @checkUser.byReligionId(authentication, #id)")
    public Religion update(@RequestBody Religion religion, @PathVariable Long id) {
        if (religion.getId() != id) {
          throw new EntityIdMismatchException();
        }
        this.religionRepository.findById(id)
          .orElseThrow(EntityNotFoundException::new);
        return this.religionRepository.save(religion);
    }
}