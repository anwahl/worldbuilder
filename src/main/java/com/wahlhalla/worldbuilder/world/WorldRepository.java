package com.wahlhalla.worldbuilder.world;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;


public interface WorldRepository extends JpaRepository<World, Long> {

    List<World> findByNameContainingIgnoreCase(String name);
    List<World> findByUserUsernameContainingIgnoreCase(String username);
    List<World> findByDescriptionContainingIgnoreCase(String description);
    List<World> findByUserId(Long user);
    List<World> findByIsPrivateFalse();
  
}
