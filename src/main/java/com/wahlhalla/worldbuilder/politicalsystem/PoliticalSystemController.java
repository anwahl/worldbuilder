package com.wahlhalla.worldbuilder.politicalsystem;


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
@RequestMapping("/api/politicalSystem")
public class PoliticalSystemController {
    
    @Autowired
    private PoliticalSystemRepository politicalSystemRepository;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<PoliticalSystem> findAll() {
        return this.politicalSystemRepository.findAll();
    }

    @GetMapping("/public")
    public List<PoliticalSystem> findAllPublic(@PathVariable Long worldId) {
        return this.politicalSystemRepository.findByWorldIdAndWorldIsPrivateFalse(worldId);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @checkUser.byPoliticalSystemId(authentication, #id)"
                   + " or @checkPrivacy.byPoliticalSystemId(#id)")
    public PoliticalSystem findOne(@PathVariable Long id) {
        return this.politicalSystemRepository.findById(id)
          .orElseThrow(EntityNotFoundException::new);
    }

    @GetMapping("/world/{worldId}")
    @PreAuthorize("hasRole('ADMIN') or @checkUser.byWorldId(authentication, #worldId)"
                  + " or @checkPrivacy.byWorldId(#worldId)")
    public List<PoliticalSystem> findByWorldId(@PathVariable Long worldId) {
        return this.politicalSystemRepository.findByWorldId(worldId);
    }

    @GetMapping("/world/type/{worldId}/{type}")
    @PreAuthorize("hasRole('ADMIN') or @checkUser.byWorldId(authentication, #worldId)"
                  + " or @checkPrivacy.byWorldId(#worldId)")
    public List<PoliticalSystem> findByWorldIdAndType(@PathVariable Long worldId,
                                                        @PathVariable PoliticalSystemType type) {
        return this.politicalSystemRepository.findByWorldIdAndType(worldId, type);
    }

    @GetMapping("/name/{name}")
    @PreAuthorize("hasRole('ADMIN')")
    public List<PoliticalSystem> findByName(@PathVariable String name) {
        return this.politicalSystemRepository.findByNameContainingIgnoreCase(name);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('USER')")
    public PoliticalSystem create(@RequestBody PoliticalSystem politicalSystem) {
        return this.politicalSystemRepository.save(politicalSystem);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @checkUser.byPoliticalSystemId(authentication, #id)")
    public void delete(@PathVariable Long id) {
        this.politicalSystemRepository.findById(id)
          .orElseThrow(EntityNotFoundException::new);
        this.politicalSystemRepository.deleteById(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @checkUser.byPoliticalSystemId(authentication, #id)")
    public PoliticalSystem update(@RequestBody PoliticalSystem politicalSystem, @PathVariable Long id) {
        if (politicalSystem.getId() != id) {
          throw new EntityIdMismatchException();
        }
        this.politicalSystemRepository.findById(id)
          .orElseThrow(EntityNotFoundException::new);
        return this.politicalSystemRepository.save(politicalSystem);
    }
}