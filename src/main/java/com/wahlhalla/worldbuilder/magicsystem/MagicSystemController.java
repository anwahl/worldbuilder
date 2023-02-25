package com.wahlhalla.worldbuilder.magicsystem;


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
@RequestMapping("/api/magicSystem")
public class MagicSystemController {
    
    @Autowired
    private MagicSystemRepository magicSystemRepository;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<MagicSystem> findAll() {
        return this.magicSystemRepository.findAll();
    }

    @GetMapping("/public")
    public List<MagicSystem> findAllPublic(@PathVariable Long worldId) {
        return this.magicSystemRepository.findByWorldIdAndWorldIsPrivateFalse(worldId);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @checkUser.byMagicSystemId(authentication, #id)"
                   + " or @checkPrivacy.byMagicSystemId(#id)")
    public MagicSystem findOne(@PathVariable Long id) {
        return this.magicSystemRepository.findById(id)
          .orElseThrow(EntityNotFoundException::new);
    }

    @GetMapping("/world/{worldId}")
    @PreAuthorize("hasRole('ADMIN') or @checkUser.byWorldId(authentication, #worldId)"
                  + " or @checkPrivacy.byWorldId(#worldId)")
    public List<MagicSystem> findByWorldId(@PathVariable Long worldId) {
        return this.magicSystemRepository.findByWorldId(worldId);
    }

    @GetMapping("/name/{name}")
    @PreAuthorize("hasRole('ADMIN')")
    public List<MagicSystem> findByName(@PathVariable String name) {
        return this.magicSystemRepository.findByNameContainingIgnoreCase(name);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('USER')")
    public MagicSystem create(@RequestBody MagicSystem magicSystem) {
        return this.magicSystemRepository.save(magicSystem);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @checkUser.byMagicSystemId(authentication, #id)")
    public void delete(@PathVariable Long id) {
        this.magicSystemRepository.findById(id)
          .orElseThrow(EntityNotFoundException::new);
        this.magicSystemRepository.deleteById(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @checkUser.byMagicSystemId(authentication, #id)")
    public MagicSystem update(@RequestBody MagicSystem magicSystem, @PathVariable Long id) {
        if (magicSystem.getId() != id) {
          throw new EntityIdMismatchException();
        }
        this.magicSystemRepository.findById(id)
          .orElseThrow(EntityNotFoundException::new);
        return this.magicSystemRepository.save(magicSystem);
    }
}
