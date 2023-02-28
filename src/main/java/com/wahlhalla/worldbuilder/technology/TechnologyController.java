package com.wahlhalla.worldbuilder.technology;


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
@RequestMapping("/api/technology")
public class TechnologyController {
    
    @Autowired
    private TechnologyRepository technologyRepository;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<Technology> findAll() {
        return this.technologyRepository.findAll();
    }

    @GetMapping("/public")
    public List<Technology> findAllPublic(@PathVariable Long worldId) {
        return this.technologyRepository.findByWorldIdAndWorldIsPrivateFalse(worldId);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @checkUser.byTechnologyId(authentication, #id)"
                   + " or @checkPrivacy.byTechnologyId(#id)")
    public Technology findOne(@PathVariable Long id) {
        return this.technologyRepository.findById(id)
          .orElseThrow(EntityNotFoundException::new);
    }

    @GetMapping("/world/{worldId}")
    @PreAuthorize("hasRole('ADMIN') or @checkUser.byWorldId(authentication, #worldId)"
                  + " or @checkPrivacy.byWorldId(#worldId)")
    public List<Technology> findByWorldId(@PathVariable Long worldId) {
        return this.technologyRepository.findByWorldId(worldId);
    }

    @GetMapping("/name/{name}")
    @PreAuthorize("hasRole('ADMIN')")
    public List<Technology> findByName(@PathVariable String name) {
        return this.technologyRepository.findByNameContainingIgnoreCase(name);
    }

    @Transactional
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('USER')")
    public Technology create(@RequestBody Technology technology) {
        return this.technologyRepository.save(technology);
    }

    @Transactional
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @checkUser.byTechnologyId(authentication, #id)")
    public void delete(@PathVariable Long id) {
        this.technologyRepository.findById(id)
          .orElseThrow(EntityNotFoundException::new);
        this.technologyRepository.deleteById(id);
    }

    @Transactional
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @checkUser.byTechnologyId(authentication, #id)")
    public Technology update(@RequestBody Technology technology, @PathVariable Long id) {
        if (technology.getId() != id) {
          throw new EntityIdMismatchException();
        }
        this.technologyRepository.findById(id)
          .orElseThrow(EntityNotFoundException::new);
        return this.technologyRepository.save(technology);
    }
}
