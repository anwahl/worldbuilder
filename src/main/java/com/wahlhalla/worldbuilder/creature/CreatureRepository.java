package com.wahlhalla.worldbuilder.creature;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CreatureRepository extends JpaRepository<Creature, Long> {

    List<Creature> findByNameContainingIgnoreCase(String name);
    List<Creature> findByWorldIdAndWorldIsPrivateFalse(Long world);
    List<Creature> findByWorldId(Long world);
}
