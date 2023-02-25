
package com.wahlhalla.worldbuilder.language;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface LanguageRepository extends JpaRepository<Language, Long> {

    List<Language> findByNameContainingIgnoreCase(String name);
    List<Language> findByWorldIdAndWorldIsPrivateFalse(Long world);
    List<Language> findByWorldId(Long world);
}
   
