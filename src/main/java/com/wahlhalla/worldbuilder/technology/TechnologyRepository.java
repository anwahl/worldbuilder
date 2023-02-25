package com.wahlhalla.worldbuilder.technology;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TechnologyRepository extends JpaRepository<Technology, Long> {

    List<Technology> findByNameContainingIgnoreCase(String name);
    List<Technology> findByWorldIdAndWorldIsPrivateFalse(Long world);
    List<Technology> findByWorldId(Long world);
}