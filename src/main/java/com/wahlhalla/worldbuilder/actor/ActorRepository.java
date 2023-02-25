package com.wahlhalla.worldbuilder.actor;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActorRepository extends JpaRepository<Actor, Long> {

    List<Actor> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(String name1, String name2);
    List<Actor> findByWorldIdAndWorldIsPrivateFalse(Long world);
    List<Actor> findByWorldId(Long world);
}
