package com.wahlhalla.worldbuilder.geography;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GeographyRepository extends JpaRepository<Geography, Long> {

    List<Geography> findByNameContainingIgnoreCase(String name);
    List<Geography> findByWorldIdAndWorldIsPrivateFalse(Long world);
    List<Geography> findByWorldId(Long world);
    List<Geography> findByWorldIdAndIdIsNot(Long world, Long geography);
}
