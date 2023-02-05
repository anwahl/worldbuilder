package com.wahlhalla.worldbuilder.race;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;


public interface RaceRepository extends JpaRepository<Race, Long> {

  List<Race> findByNameContainingIgnoreCase(String name);
  List<Race> findByWorldIsPrivateFalse();
  List<Race> findByWorldId(Long world);

}
