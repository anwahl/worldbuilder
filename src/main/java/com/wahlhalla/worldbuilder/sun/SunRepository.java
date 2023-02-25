package com.wahlhalla.worldbuilder.sun;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SunRepository extends JpaRepository<Sun, Long> {

    List<Sun> findByNameContainingIgnoreCase(String name);
    List<Sun> findByWorldIdAndWorldIsPrivateFalse(Long world);
    List<Sun> findByWorldId(Long world);
}
