package com.wahlhalla.worldbuilder.plant;



import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlantRepository extends JpaRepository<Plant, Long> {

    List<Plant> findByNameContainingIgnoreCase(String name);
    List<Plant> findByWorldIdAndWorldIsPrivateFalse(Long world);
    List<Plant> findByWorldId(Long world);
}
