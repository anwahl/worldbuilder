package com.wahlhalla.worldbuilder.socialclass;


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
@RequestMapping("/api/socialClass")
public class SocialClassController {
    
    @Autowired
    private SocialClassRepository socialClassRepository;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<SocialClass> findAll() {
        return this.socialClassRepository.findAll();
    }

    @GetMapping("/public")
    public List<SocialClass> findAllPublic(@PathVariable Long worldId) {
        return this.socialClassRepository.findByWorldIdAndWorldIsPrivateFalse(worldId);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @checkUser.bySocialClassId(authentication, #id)"
                   + " or @checkPrivacy.bySocialClassId(#id)")
    public SocialClass findOne(@PathVariable Long id) {
        return this.socialClassRepository.findById(id)
          .orElseThrow(EntityNotFoundException::new);
    }

    @GetMapping("/world/{worldId}")
    @PreAuthorize("hasRole('ADMIN') or @checkUser.byWorldId(authentication, #worldId)"
                  + " or @checkPrivacy.byWorldId(#worldId)")
    public List<SocialClass> findByWorldId(@PathVariable Long worldId) {
        return this.socialClassRepository.findByWorldId(worldId);
    }

    @GetMapping("/name/{name}")
    @PreAuthorize("hasRole('ADMIN')")
    public List<SocialClass> findByName(@PathVariable String name) {
        return this.socialClassRepository.findByNameContainingIgnoreCase(name);
    }

    @GetMapping("/world/region/{worldId}/{regionId}")
    @PreAuthorize("hasRole('ADMIN')")
    public List<SocialClass> findByRegion(@PathVariable long worldId, @PathVariable long regionId) {
        return this.socialClassRepository.findByWorldIdAndRegionId(worldId, regionId);
    }

    @GetMapping("/world/race/{worldId}/{raceId}")
    @PreAuthorize("hasRole('ADMIN')")
    public List<SocialClass> findByRace(@PathVariable long worldId, @PathVariable long raceId) {
        return this.socialClassRepository.findByWorldIdAndRegionId(worldId, raceId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('USER')")
    public SocialClass create(@RequestBody SocialClass socialClass) {
        return this.socialClassRepository.save(socialClass);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @checkUser.bySocialClassId(authentication, #id)")
    public void delete(@PathVariable Long id) {
        this.socialClassRepository.findById(id)
          .orElseThrow(EntityNotFoundException::new);
        this.socialClassRepository.deleteById(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @checkUser.bySocialClassId(authentication, #id)")
    public SocialClass update(@RequestBody SocialClass socialClass, @PathVariable Long id) {
        if (socialClass.getId() != id) {
          throw new EntityIdMismatchException();
        }
        this.socialClassRepository.findById(id)
          .orElseThrow(EntityNotFoundException::new);
        return this.socialClassRepository.save(socialClass);
    }
}