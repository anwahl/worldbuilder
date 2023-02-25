package com.wahlhalla.worldbuilder.actor;


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
@RequestMapping("/api/actor")
public class ActorController {
    
    @Autowired
    private ActorRepository actorRepository;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<Actor> findAll() {
        return this.actorRepository.findAll();
    }

    @GetMapping("/public")
    public List<Actor> findAllPublic(@PathVariable Long worldId) {
        return this.actorRepository.findByWorldIdAndWorldIsPrivateFalse(worldId);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @checkUser.byActorId(authentication, #id)"
                   + " or @checkPrivacy.byActorId(#id)")
    public Actor findOne(@PathVariable Long id) {
        return this.actorRepository.findById(id)
          .orElseThrow(EntityNotFoundException::new);
    }

    @GetMapping("/world/{worldId}")
    @PreAuthorize("hasRole('ADMIN') or @checkUser.byWorldId(authentication, #worldId)"
                  + " or @checkPrivacy.byWorldId(#worldId)")
    public List<Actor> findByWorldId(@PathVariable Long worldId) {
        return this.actorRepository.findByWorldId(worldId);
    }

    @GetMapping("/name/{name}")
    @PreAuthorize("hasRole('ADMIN')")
    public List<Actor> findByName(@PathVariable String name) {
        return this.actorRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(name, name);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('USER')")
    public Actor create(@RequestBody Actor actor) {
        return this.actorRepository.save(actor);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @checkUser.byActorId(authentication, #id)")
    public void delete(@PathVariable Long id) {
        this.actorRepository.findById(id)
          .orElseThrow(EntityNotFoundException::new);
        this.actorRepository.deleteById(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @checkUser.byActorId(authentication, #id)")
    public Actor update(@RequestBody Actor actor, @PathVariable Long id) {
        if (actor.getId() != id) {
          throw new EntityIdMismatchException();
        }
        this.actorRepository.findById(id)
          .orElseThrow(EntityNotFoundException::new);
        return this.actorRepository.save(actor);
    }
}
