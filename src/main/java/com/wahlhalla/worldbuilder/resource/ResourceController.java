package com.wahlhalla.worldbuilder.resource;


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
@RequestMapping("/api/resource")
public class ResourceController {
    
    @Autowired
    private ResourceRepository resourceRepository;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<Resource> findAll() {
        return this.resourceRepository.findAll();
    }

    @GetMapping("/public")
    public List<Resource> findAllPublic(@PathVariable Long worldId) {
        return this.resourceRepository.findByWorldIdAndWorldIsPrivateFalse(worldId);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @checkUser.byResourceId(authentication, #id)"
                   + " or @checkPrivacy.byResourceId(#id)")
    public Resource findOne(@PathVariable Long id) {
        return this.resourceRepository.findById(id)
          .orElseThrow(EntityNotFoundException::new);
    }

    @GetMapping("/world/{worldId}")
    @PreAuthorize("hasRole('ADMIN') or @checkUser.byWorldId(authentication, #worldId)"
                  + " or @checkPrivacy.byWorldId(#worldId)")
    public List<Resource> findByWorldId(@PathVariable Long worldId) {
        return this.resourceRepository.findByWorldId(worldId);
    }

    @GetMapping("/name/{name}")
    @PreAuthorize("hasRole('ADMIN')")
    public List<Resource> findByName(@PathVariable String name) {
        return this.resourceRepository.findByNameContainingIgnoreCase(name);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('USER')")
    public Resource create(@RequestBody Resource resource) {
        return this.resourceRepository.save(resource);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @checkUser.byResourceId(authentication, #id)")
    public void delete(@PathVariable Long id) {
        this.resourceRepository.findById(id)
          .orElseThrow(EntityNotFoundException::new);
        this.resourceRepository.deleteById(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @checkUser.byResourceId(authentication, #id)")
    public Resource update(@RequestBody Resource resource, @PathVariable Long id) {
        if (resource.getId() != id) {
          throw new EntityIdMismatchException();
        }
        this.resourceRepository.findById(id)
          .orElseThrow(EntityNotFoundException::new);
        return this.resourceRepository.save(resource);
    }
}
