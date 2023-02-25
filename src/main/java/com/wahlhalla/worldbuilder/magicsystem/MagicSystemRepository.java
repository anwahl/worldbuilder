package com.wahlhalla.worldbuilder.magicsystem;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MagicSystemRepository extends JpaRepository<MagicSystem, Long> {

    List<MagicSystem> findByNameContainingIgnoreCase(String name);
    List<MagicSystem> findByWorldIdAndWorldIsPrivateFalse(Long world);
    List<MagicSystem> findByWorldId(Long world);
}
