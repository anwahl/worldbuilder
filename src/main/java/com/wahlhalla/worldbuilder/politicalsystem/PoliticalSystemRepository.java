package com.wahlhalla.worldbuilder.politicalsystem;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PoliticalSystemRepository extends JpaRepository<PoliticalSystem, Long> {

    List<PoliticalSystem> findByNameContainingIgnoreCase(String name);
    List<PoliticalSystem> findByWorldIdAndWorldIsPrivateFalse(Long world);
    List<PoliticalSystem> findByWorldId(Long world);
    List<PoliticalSystem> findByWorldIdAndType(Long world, PoliticalSystemType type);
}