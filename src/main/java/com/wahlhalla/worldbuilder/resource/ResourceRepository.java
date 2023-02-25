package com.wahlhalla.worldbuilder.resource;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResourceRepository extends JpaRepository<Resource, Long> {

    List<Resource> findByNameContainingIgnoreCase(String name);
    List<Resource> findByWorldIdAndWorldIsPrivateFalse(Long world);
    List<Resource> findByWorldId(Long world);
}