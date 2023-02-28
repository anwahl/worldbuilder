package com.wahlhalla.worldbuilder.god;


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
@RequestMapping("/api/god")
public class GodController {
    
    @Autowired
    private GodRepository godRepository;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<God> findAll() {
        return this.godRepository.findAll();
    }

    @GetMapping("/public")
    public List<God> findAllPublic(@PathVariable Long worldId) {
        return this.godRepository.findByWorldIdAndWorldIsPrivateFalse(worldId);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @checkUser.byGodId(authentication, #id)"
                   + " or @checkPrivacy.byGodId(#id)")
    public God findOne(@PathVariable Long id) {
        return this.godRepository.findById(id)
          .orElseThrow(EntityNotFoundException::new);
    }

    @GetMapping("/world/{worldId}")
    @PreAuthorize("hasRole('ADMIN') or @checkUser.byWorldId(authentication, #worldId)"
                  + " or @checkPrivacy.byWorldId(#worldId)")
    public List<God> findByWorldId(@PathVariable Long worldId) {
        return this.godRepository.findByWorldId(worldId);
    }

    @GetMapping("/name/{name}")
    @PreAuthorize("hasRole('ADMIN')")
    public List<God> findByName(@PathVariable String name) {
        return this.godRepository.findByNameContainingIgnoreCase(name);
    }

    @Transactional
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('USER')")
    public God create(@RequestBody God god) {
        return this.godRepository.save(god);
    }

    @Transactional
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @checkUser.byGodId(authentication, #id)")
    public void delete(@PathVariable Long id) {
        this.godRepository.findById(id)
          .orElseThrow(EntityNotFoundException::new);
        this.godRepository.deleteById(id);
    }

    @Transactional
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @checkUser.byGodId(authentication, #id)")
    public God update(@RequestBody God god, @PathVariable Long id) {
        if (god.getId() != id) {
          throw new EntityIdMismatchException();
        }
        this.godRepository.findById(id)
          .orElseThrow(EntityNotFoundException::new);
        return this.godRepository.save(god);
    }
}