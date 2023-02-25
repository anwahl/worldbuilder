package com.wahlhalla.worldbuilder.creature;


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
@RequestMapping("/api/creature")
public class CreatureController {
    
    @Autowired
    private CreatureRepository creatureRepository;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<Creature> findAll() {
        return this.creatureRepository.findAll();
    }

    @GetMapping("/public")
    public List<Creature> findAllPublic(@PathVariable Long worldId) {
        return this.creatureRepository.findByWorldIdAndWorldIsPrivateFalse(worldId);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @checkUser.byCreatureId(authentication, #id)"
                   + " or @checkPrivacy.byCreatureId(#id)")
    public Creature findOne(@PathVariable Long id) {
        return this.creatureRepository.findById(id)
          .orElseThrow(EntityNotFoundException::new);
    }

    @GetMapping("/world/{worldId}")
    @PreAuthorize("hasRole('ADMIN') or @checkUser.byWorldId(authentication, #worldId)"
                  + " or @checkPrivacy.byWorldId(#worldId)")
    public List<Creature> findByWorldId(@PathVariable Long worldId) {
        return this.creatureRepository.findByWorldId(worldId);
    }

    @GetMapping("/name/{name}")
    @PreAuthorize("hasRole('ADMIN')")
    public List<Creature> findByName(@PathVariable String name) {
        return this.creatureRepository.findByNameContainingIgnoreCase(name);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('USER')")
    public Creature create(@RequestBody Creature creature) {
        return this.creatureRepository.save(creature);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @checkUser.byCreatureId(authentication, #id)")
    public void delete(@PathVariable Long id) {
        this.creatureRepository.findById(id)
          .orElseThrow(EntityNotFoundException::new);
        this.creatureRepository.deleteById(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @checkUser.byCreatureId(authentication, #id)")
    public Creature update(@RequestBody Creature creature, @PathVariable Long id) {
        if (creature.getId() != id) {
          throw new EntityIdMismatchException();
        }
        this.creatureRepository.findById(id)
          .orElseThrow(EntityNotFoundException::new);
        return this.creatureRepository.save(creature);
    }
}