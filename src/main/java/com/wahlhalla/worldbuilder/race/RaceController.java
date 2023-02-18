package com.wahlhalla.worldbuilder.race;

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
@RequestMapping("/api/race")
public class RaceController {
    
    @Autowired
    private RaceRepository raceRepository;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<Race> findAll() {
        return this.raceRepository.findAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @checkUser.byRaceId(authentication, #id)"
                   + " or @checkPrivacy.byRaceId(#id)")
    public Race findOne(@PathVariable Long id) {
        return this.raceRepository.findById(id)
          .orElseThrow(EntityNotFoundException::new);
    }

    @GetMapping("/world/{worldId}")
    @PreAuthorize("hasRole('ADMIN') or @checkUser.byWorldId(authentication, #worldId)"
                  + " or @checkPrivacy.byWorldId(#worldId)")
    public List<Race> findByWorldId(@PathVariable Long worldId) {
        return this.raceRepository.findByWorldId(worldId);
    }

    @GetMapping("/name/{name}")
    @PreAuthorize("hasRole('ADMIN')")
    public List<Race> findByName(@PathVariable String name) {
        return this.raceRepository.findByNameContainingIgnoreCase(name);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('USER')")
    public Race create(@RequestBody Race race) {
        return this.raceRepository.save(race);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @checkUser.byRaceId(authentication, #id)")
    public void delete(@PathVariable Long id) {
        this.raceRepository.findById(id)
          .orElseThrow(EntityNotFoundException::new);
        this.raceRepository.deleteById(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @checkUser.byRaceId(authentication, #id)")
    public Race update(@RequestBody Race race, @PathVariable Long id) {
        if (race.getId() != id) {
          throw new EntityIdMismatchException();
        }
        this.raceRepository.findById(id)
          .orElseThrow(EntityNotFoundException::new);
        return this.raceRepository.save(race);
    }
}
