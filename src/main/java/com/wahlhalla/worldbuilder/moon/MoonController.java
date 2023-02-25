package com.wahlhalla.worldbuilder.moon;


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
@RequestMapping("/api/moon")
public class MoonController {
    
    @Autowired
    private MoonRepository moonRepository;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<Moon> findAll() {
        return this.moonRepository.findAll();
    }

    @GetMapping("/public")
    public List<Moon> findAllPublic(@PathVariable Long worldId) {
        return this.moonRepository.findByWorldIdAndWorldIsPrivateFalse(worldId);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @checkUser.byMoonId(authentication, #id)"
                   + " or @checkPrivacy.byMoonId(#id)")
    public Moon findOne(@PathVariable Long id) {
        return this.moonRepository.findById(id)
          .orElseThrow(EntityNotFoundException::new);
    }

    @GetMapping("/world/{worldId}")
    @PreAuthorize("hasRole('ADMIN') or @checkUser.byWorldId(authentication, #worldId)"
                  + " or @checkPrivacy.byWorldId(#worldId)")
    public List<Moon> findByWorldId(@PathVariable Long worldId) {
        return this.moonRepository.findByWorldId(worldId);
    }

    @GetMapping("/name/{name}")
    @PreAuthorize("hasRole('ADMIN')")
    public List<Moon> findByName(@PathVariable String name) {
        return this.moonRepository.findByNameContainingIgnoreCase(name);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('USER')")
    public Moon create(@RequestBody Moon moon) {
        return this.moonRepository.save(moon);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @checkUser.byMoonId(authentication, #id)")
    public void delete(@PathVariable Long id) {
        this.moonRepository.findById(id)
          .orElseThrow(EntityNotFoundException::new);
        this.moonRepository.deleteById(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @checkUser.byMoonId(authentication, #id)")
    public Moon update(@RequestBody Moon moon, @PathVariable Long id) {
        if (moon.getId() != id) {
          throw new EntityIdMismatchException();
        }
        this.moonRepository.findById(id)
          .orElseThrow(EntityNotFoundException::new);
        return this.moonRepository.save(moon);
    }
}
