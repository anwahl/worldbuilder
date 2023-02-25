package com.wahlhalla.worldbuilder.timeline;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TimelineEventRepository extends JpaRepository<TimelineEvent, Long> {

    List<TimelineEvent> findByDate(Double date);
    List<TimelineEvent> findByWorldIdAndWorldIsPrivateFalse(Long world);
    List<TimelineEvent> findByWorldId(Long world);
}