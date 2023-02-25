package com.wahlhalla.worldbuilder.race;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;


public interface RaceRepository extends JpaRepository<Race, Long> {

  List<Race> findByNameContainingIgnoreCase(String name);
  List<Race> findByWorldIdAndWorldIsPrivateFalse(Long world);
  List<Race> findByWorldId(Long world);

}
