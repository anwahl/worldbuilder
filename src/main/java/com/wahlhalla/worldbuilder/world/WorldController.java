package com.wahlhalla.worldbuilder.world;

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
@RequestMapping("/api/world")
public class WorldController {

    @Autowired
    private WorldRepository worldRepository;
    
    @Autowired
    private WorldService worldService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<World> findAll() {
        return this.worldRepository.findAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @checkUser.byWorldId(authentication, #id)"
                   + " or @checkPrivacy.byWorldId(#id)")
    public World findOne(@PathVariable Long id) {
        return this.worldRepository.findById(id)
          .orElseThrow(EntityNotFoundException::new);
    }

    @GetMapping("/public")
    public List<World> findPublic() {
        return this.worldRepository.findByIsPrivateFalse();
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public List<World> findByUserId(@PathVariable Long userId) {
        return this.worldRepository.findByUserId(userId);
    }

    @GetMapping("/name/{name}")
    @PreAuthorize("hasRole('ADMIN')")
    public List<World> findByName(@PathVariable String name) {
        return this.worldRepository.findByNameContainingIgnoreCase(name);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('USER')")
    public World create(@RequestBody World world) {
        return this.worldService.save(world);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @checkUser.byWorldId(authentication, #id)")
    public void delete(@PathVariable Long id) {
        this.worldRepository.findById(id)
          .orElseThrow(EntityNotFoundException::new);
        this.worldRepository.deleteById(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @checkUser.byWorldId(authentication, #id)")
    public World update(@RequestBody World world, @PathVariable Long id) {
        if (world.getId() != id) {
          throw new EntityIdMismatchException();
        }
        this.worldRepository.findById(id)
          .orElseThrow(EntityNotFoundException::new);
        return this.worldService.save(world);
    }
}
