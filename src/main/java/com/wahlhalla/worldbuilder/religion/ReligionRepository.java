package com.wahlhalla.worldbuilder.religion;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReligionRepository extends JpaRepository<Religion, Long> {

    List<Religion> findByNameContainingIgnoreCase(String name);
    List<Religion> findByWorldIdAndWorldIsPrivateFalse(Long world);
    List<Religion> findByWorldId(Long world);

}
