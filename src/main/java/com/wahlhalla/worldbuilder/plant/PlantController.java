package com.wahlhalla.worldbuilder.plant;



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
@RequestMapping("/api/plant")
public class PlantController {
    
    @Autowired
    private PlantRepository plantRepository;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<Plant> findAll() {
        return this.plantRepository.findAll();
    }

    @GetMapping("/public")
    public List<Plant> findAllPublic(@PathVariable Long worldId) {
        return this.plantRepository.findByWorldIdAndWorldIsPrivateFalse(worldId);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @checkUser.byPlantId(authentication, #id)"
                   + " or @checkPrivacy.byPlantId(#id)")
    public Plant findOne(@PathVariable Long id) {
        return this.plantRepository.findById(id)
          .orElseThrow(EntityNotFoundException::new);
    }

    @GetMapping("/world/{worldId}")
    @PreAuthorize("hasRole('ADMIN') or @checkUser.byWorldId(authentication, #worldId)"
                  + " or @checkPrivacy.byWorldId(#worldId)")
    public List<Plant> findByWorldId(@PathVariable Long worldId) {
        return this.plantRepository.findByWorldId(worldId);
    }

    @GetMapping("/name/{name}")
    @PreAuthorize("hasRole('ADMIN')")
    public List<Plant> findByName(@PathVariable String name) {
        return this.plantRepository.findByNameContainingIgnoreCase(name);
    }

    @Transactional
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('USER')")
    public Plant create(@RequestBody Plant plant) {
        return this.plantRepository.save(plant);
    }

    @Transactional
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @checkUser.byPlantId(authentication, #id)")
    public void delete(@PathVariable Long id) {
        this.plantRepository.findById(id)
          .orElseThrow(EntityNotFoundException::new);
        this.plantRepository.deleteById(id);
    }

    @Transactional
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @checkUser.byPlantId(authentication, #id)")
    public Plant update(@RequestBody Plant plant, @PathVariable Long id) {
        if (plant.getId() != id) {
          throw new EntityIdMismatchException();
        }
        this.plantRepository.findById(id)
          .orElseThrow(EntityNotFoundException::new);
        return this.plantRepository.save(plant);
    }
}
