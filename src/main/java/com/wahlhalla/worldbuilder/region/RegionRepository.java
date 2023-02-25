package com.wahlhalla.worldbuilder.region;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RegionRepository extends JpaRepository<Region, Long> {

    List<Region> findByNameContainingIgnoreCase(String name);
    List<Region> findByWorldIdAndWorldIsPrivateFalse(Long world);
    List<Region> findByWorldId(Long world);
    List<Region> findByWorldIdAndJurisdictionId(Long world, Long jurisdiction);
    List<Region> findByWorldIdAndMunicipality(Long world, Municipality municipality);
    List<Region> findByWorldIdAndMunicipalityAndJurisdictionId(Long world, Municipality municipality, Long jurisdiction);
}
