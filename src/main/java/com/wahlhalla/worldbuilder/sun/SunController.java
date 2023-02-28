package com.wahlhalla.worldbuilder.sun;


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
@RequestMapping("/api/sun")
public class SunController {
    
    @Autowired
    private SunRepository sunRepository;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<Sun> findAll() {
        return this.sunRepository.findAll();
    }

    @GetMapping("/public")
    public List<Sun> findAllPublic(@PathVariable Long worldId) {
        return this.sunRepository.findByWorldIdAndWorldIsPrivateFalse(worldId);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @checkUser.bySunId(authentication, #id)"
                   + " or @checkPrivacy.bySunId(#id)")
    public Sun findOne(@PathVariable Long id) {
        return this.sunRepository.findById(id)
          .orElseThrow(EntityNotFoundException::new);
    }

    @GetMapping("/world/{worldId}")
    @PreAuthorize("hasRole('ADMIN') or @checkUser.byWorldId(authentication, #worldId)"
                  + " or @checkPrivacy.byWorldId(#worldId)")
    public List<Sun> findByWorldId(@PathVariable Long worldId) {
        return this.sunRepository.findByWorldId(worldId);
    }

    @GetMapping("/name/{name}")
    @PreAuthorize("hasRole('ADMIN')")
    public List<Sun> findByName(@PathVariable String name) {
        return this.sunRepository.findByNameContainingIgnoreCase(name);
    }

    @Transactional
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('USER')")
    public Sun create(@RequestBody Sun sun) {
        return this.sunRepository.save(sun);
    }

    @Transactional
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @checkUser.bySunId(authentication, #id)")
    public void delete(@PathVariable Long id) {
        this.sunRepository.findById(id)
          .orElseThrow(EntityNotFoundException::new);
        this.sunRepository.deleteById(id);
    }

    @Transactional
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @checkUser.bySunId(authentication, #id)")
    public Sun update(@RequestBody Sun sun, @PathVariable Long id) {
        if (sun.getId() != id) {
          throw new EntityIdMismatchException();
        }
        this.sunRepository.findById(id)
          .orElseThrow(EntityNotFoundException::new);
        return this.sunRepository.save(sun);
    }
}
