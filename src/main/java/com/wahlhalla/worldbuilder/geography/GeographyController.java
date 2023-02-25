package com.wahlhalla.worldbuilder.geography;


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
@RequestMapping("/api/geography")
public class GeographyController {
    
    @Autowired
    private GeographyRepository geographyRepository;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<Geography> findAll() {
        return this.geographyRepository.findAll();
    }

    @GetMapping("/public")
    public List<Geography> findAllPublic(@PathVariable Long worldId) {
        return this.geographyRepository.findByWorldIdAndWorldIsPrivateFalse(worldId);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @checkUser.byGeographyId(authentication, #id)"
                   + " or @checkPrivacy.byGeographyId(#id)")
    public Geography findOne(@PathVariable Long id) {
        return this.geographyRepository.findById(id)
          .orElseThrow(EntityNotFoundException::new);
    }

    @GetMapping("/world/{worldId}")
    @PreAuthorize("hasRole('ADMIN') or @checkUser.byWorldId(authentication, #worldId)"
                  + " or @checkPrivacy.byWorldId(#worldId)")
    public List<Geography> findByWorldId(@PathVariable Long worldId) {
        return this.geographyRepository.findByWorldId(worldId);
    }

    @GetMapping("/name/{name}")
    @PreAuthorize("hasRole('ADMIN')")
    public List<Geography> findByName(@PathVariable String name) {
        return this.geographyRepository.findByNameContainingIgnoreCase(name);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('USER')")
    public Geography create(@RequestBody Geography geography) {
        return this.geographyRepository.save(geography);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @checkUser.byGeographyId(authentication, #id)")
    public void delete(@PathVariable Long id) {
        this.geographyRepository.findById(id)
          .orElseThrow(EntityNotFoundException::new);
        this.geographyRepository.deleteById(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @checkUser.byGeographyId(authentication, #id)")
    public Geography update(@RequestBody Geography geography, @PathVariable Long id) {
        if (geography.getId() != id) {
          throw new EntityIdMismatchException();
        }
        this.geographyRepository.findById(id)
          .orElseThrow(EntityNotFoundException::new);
        return this.geographyRepository.save(geography);
    }
}
