package com.wahlhalla.worldbuilder.region;

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
@RequestMapping("/api/region")
public class RegionController {
    
    @Autowired
    private RegionRepository regionRepository;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<Region> findAll() {
        return this.regionRepository.findAll();
    }

    @GetMapping("/public")
    public List<Region> findAllPublic(@PathVariable Long worldId) {
        return this.regionRepository.findByWorldIdAndWorldIsPrivateFalse(worldId);
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @checkUser.byRegionId(authentication, #id)"
                   + " or @checkPrivacy.byRegionId(#id)")
    public Region findOne(@PathVariable Long id) {
        return this.regionRepository.findById(id)
          .orElseThrow(EntityNotFoundException::new);
    }
    
    @GetMapping("/world/{worldId}")
    @PreAuthorize("hasRole('ADMIN') or @checkUser.byWorldId(authentication, #worldId)"
                  + " or @checkPrivacy.byWorldId(#worldId)")
    public List<Region> findByWorldId(@PathVariable Long worldId) {
        return this.regionRepository.findByWorldId(worldId);
    }
    
    @GetMapping("/world/jurisdiction/{worldId}/{jurisdictionId}")
    @PreAuthorize("hasRole('ADMIN') or @checkUser.byWorldId(authentication, #worldId)"
                  + " or @checkPrivacy.byWorldId(#worldId)")
    public List<Region> findByWorldIdAndJurisdictionId(@PathVariable Long worldId,
                                                     @PathVariable Long jurisdictionId) {
        return this.regionRepository.findByWorldIdAndJurisdictionId(worldId, jurisdictionId);
    }

    @GetMapping("/world/municipality/{worldId}/{municipality}")
    @PreAuthorize("hasRole('ADMIN') or @checkUser.byWorldId(authentication, #worldId)"
                  + " or @checkPrivacy.byWorldId(#worldId)")
    public List<Region> findByWorldIdAndMunicipality(@PathVariable Long worldId,
                                                     @PathVariable Municipality municipality) {
        return this.regionRepository.findByWorldIdAndMunicipality(worldId, municipality);
    }
    
    @GetMapping("/world/municipality/jurisdiction/{worldId}/{municipality}/{jurisdictionId}")
    @PreAuthorize("hasRole('ADMIN') or @checkUser.byWorldId(authentication, #worldId)"
                  + " or @checkPrivacy.byWorldId(#worldId)")
    public List<Region> findByWorldIdAndMunicipalityAndJurisdictionId(@PathVariable Long worldId,
                                                     @PathVariable Municipality municipality,
                                                     @PathVariable Long jurisdictionId) {
        return this.regionRepository.findByWorldIdAndMunicipalityAndJurisdictionId(
            worldId, municipality, jurisdictionId);
    }
    
    @GetMapping("/name/{name}")
    @PreAuthorize("hasRole('ADMIN')")
    public List<Region> findByName(@PathVariable String name) {
        return this.regionRepository.findByNameContainingIgnoreCase(name);
    }
    
    @Transactional
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('USER')")
    public Region create(@RequestBody Region region) {
        return this.regionRepository.save(region);
    }
    
    @Transactional
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @checkUser.byRegionId(authentication, #id)")
    public void delete(@PathVariable Long id) {
        Region region = this.regionRepository.findById(id)
          .orElseThrow(EntityNotFoundException::new);
        for (Region child : region.getChildRegions()) {
            child.setJurisdiction(null);
            this.regionRepository.save(child);
        }
        this.regionRepository.delete(region);
    }

    @Transactional
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @checkUser.byRegionId(authentication, #id)")
    public Region update(@RequestBody Region region, @PathVariable Long id) {
        if (region.getId() != id) {
          throw new EntityIdMismatchException();
        }
        this.regionRepository.findById(id)
          .orElseThrow(EntityNotFoundException::new);
        return this.regionRepository.save(region);
    }
}
