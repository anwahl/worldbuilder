package com.wahlhalla.worldbuilder.god;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GodRepository extends JpaRepository<God, Long> {

    List<God> findByNameContainingIgnoreCase(String name);
    List<God> findByWorldIdAndWorldIsPrivateFalse(Long world);
    List<God> findByWorldId(Long world);
}
