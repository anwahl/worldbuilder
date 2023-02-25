package com.wahlhalla.worldbuilder.socialclass;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SocialClassRepository extends JpaRepository<SocialClass, Long> {

    List<SocialClass> findByNameContainingIgnoreCase(String name);
    List<SocialClass> findByWorldIdAndRegionId(Long world, Long region);
    List<SocialClass> findByWorldIdAndRaceId(Long world, Long region);
    List<SocialClass> findByWorldIdAndWorldIsPrivateFalse(Long world);
    List<SocialClass> findByWorldId(Long world);
}
