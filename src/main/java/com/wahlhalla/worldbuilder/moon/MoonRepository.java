package com.wahlhalla.worldbuilder.moon;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MoonRepository extends JpaRepository<Moon, Long> {

    List<Moon> findByNameContainingIgnoreCase(String name);
    List<Moon> findByWorldIdAndWorldIsPrivateFalse(Long world);
    List<Moon> findByWorldId(Long world);
}
